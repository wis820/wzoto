package com.wzoto.infrastructure.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AI服务辅助工具类 - 统一AI请求处理
 * 限流、重试、超时、结构化解析
 */
@Slf4j
@Component
public class AiServiceHelper {

    private static final int MAX_RETRY = 2;
    private static final int DAILY_LIMIT = 50;

    /**
     * 校验每日调用次数是否超限
     */
    public boolean checkDailyLimit(int currentCount) {
        if (currentCount >= DAILY_LIMIT) {
            log.warn("AI调用次数超限, currentCount={}, limit={}", currentCount, DAILY_LIMIT);
            return false;
        }
        return true;
    }

    /**
     * 带重试的AI调用（预留扩展点）
     */
    public String callWithRetry(AiCallable callable) {
        Exception lastException = null;
        for (int i = 0; i <= MAX_RETRY; i++) {
            try {
                return callable.call();
            } catch (Exception e) {
                lastException = e;
                log.warn("AI调用失败, 重试次数={}/{}", i + 1, MAX_RETRY, e);
            }
        }
        log.error("AI调用最终失败", lastException);
        return null;
    }

    /**
     * 提取JSON中的指定字段
     */
    public static String extractJsonField(String json, String fieldName) {
        if (json == null || fieldName == null) return null;
        int start = json.indexOf("\"" + fieldName + "\"");
        if (start < 0) return null;
        int colonIdx = json.indexOf(":", start);
        if (colonIdx < 0) return null;
        int valueStart = json.indexOf("\"", colonIdx);
        if (valueStart < 0) return null;
        int valueEnd = json.indexOf("\"", valueStart + 1);
        if (valueEnd < 0) return null;
        return json.substring(valueStart + 1, valueEnd);
    }

    @FunctionalInterface
    public interface AiCallable {
        String call() throws Exception;
    }
}
