package com.wzoto.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产环境数据初始化器
 * 在 prod profile 下启动时自动执行：
 * 1. 建表 (init-h2-schema.sql)
 * 2. 灌入种子数据 (seed-h2-data.sql)
 *
 * 逐条执行 SQL 语句，单条失败仅记录警告不中断整体初始化
 */
@Component
@Profile("prod")
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DataSource dataSource;

    public DataInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("========== 开始初始化 H2 数据库 ==========");
        long start = System.currentTimeMillis();

        int[] schemaResult = executeScript("sql/init-h2-schema.sql", "建表");
        int[] seedResult = executeScript("sql/seed-h2-data.sql", "种子数据");

        long elapsed = System.currentTimeMillis() - start;
        log.info("========== 数据库初始化完成 ({}ms) | 建表: 成功{} 失败{} | 种子数据: 成功{} 失败{} ==========",
                elapsed,
                schemaResult[0], schemaResult[1],
                seedResult[0], seedResult[1]);
    }

    /**
     * 逐条执行 SQL 脚本，返回 [成功数, 失败数]
     */
    private int[] executeScript(String path, String label) {
        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            log.warn("SQL 文件不存在: {}", path);
            return new int[]{0, 0};
        }

        log.info("执行 {} ...", label);
        long t = System.currentTimeMillis();

        List<String> statements = splitStatements(resource);
        int success = 0, failed = 0;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                String trimmed = sql.trim();
                if (trimmed.isEmpty()) continue;
                try {
                    stmt.execute(trimmed);
                    success++;
                } catch (Exception e) {
                    failed++;
                    // 只记录前几条失败的详情，避免日志爆炸
                    if (failed <= 5) {
                        log.warn("{} 语句失败: {} | 错误: {}",
                                label, trimForLog(trimmed), e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("{} 获取连接失败: {}", label, e.getMessage());
        }

        log.info("{} 完成 ({}ms): 成功 {} 条, 失败 {} 条",
                label, System.currentTimeMillis() - t, success, failed);
        return new int[]{success, failed};
    }

    /**
     * 按分号拆分 SQL 语句（忽略引号内的分号）
     */
    private List<String> splitStatements(ClassPathResource resource) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                // 跳过纯注释行
                if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;

                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '\'' ) {
                        // 处理转义引号 ''
                        if (inSingleQuote && i + 1 < line.length() && line.charAt(i + 1) == '\'') {
                            current.append(c);
                            i++;
                            current.append(line.charAt(i));
                            continue;
                        }
                        inSingleQuote = !inSingleQuote;
                    }
                    if (c == ';' && !inSingleQuote) {
                        String stmt = current.toString().trim();
                        if (!stmt.isEmpty()) statements.add(stmt);
                        current.setLength(0);
                    } else {
                        current.append(c);
                    }
                }
                current.append('\n');
            }
            // 最后一条（无分号结尾）
            String last = current.toString().trim();
            if (!last.isEmpty()) statements.add(last);
        } catch (Exception e) {
            log.error("读取 SQL 文件失败: {}", e.getMessage());
        }

        return statements;
    }

    private String trimForLog(String sql) {
        return sql.length() > 120 ? sql.substring(0, 120) + "..." : sql;
    }
}
