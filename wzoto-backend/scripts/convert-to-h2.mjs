#!/usr/bin/env node
/**
 * MySQL DDL/种子数据 → H2 兼容格式（纯 Node.js 处理，避免 PowerShell 编码问题）
 * 直接输出到 wzoto-interfaces/src/main/resources/sql/
 */
import { readFileSync, writeFileSync, readdirSync, mkdirSync } from 'fs';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const SQL_DIR = join(__dirname, '..', 'sql');
const OUT_DIR = join(__dirname, '..', 'wzoto-interfaces', 'src', 'main', 'resources', 'sql');
mkdirSync(OUT_DIR, { recursive: true });

// ========== DDL 转换 ==========
const tableFiles = readdirSync(SQL_DIR).filter(f => f.startsWith('t_') && f.endsWith('.sql')).sort();
console.log(`Found ${tableFiles.length} table DDL files`);

function stripComments(sql) {
  // 去掉列定义中的 COMMENT 'xxx'（含转义引号 ''）
  return sql.replace(/COMMENT\s+'(?:[^']|'')*'/gi, '');
}

function convertDDL(content, filename) {
  let sql = content;

  // 去掉 CREATE DATABASE / USE / SET NAMES
  sql = sql.replace(/CREATE DATABASE[^;]*;/gi, '');
  sql = sql.replace(/USE\s+\w+\s*;/gi, '');
  sql = sql.replace(/SET NAMES[^;]*;/gi, '');

  // 去掉反引号
  sql = sql.replace(/`/g, '');

  // 去掉列 COMMENT
  sql = stripComments(sql);

  // 去掉表尾 ENGINE=... CHARSET=... COMMENT='...'
  sql = sql.replace(/\)\s*ENGINE\s*=[^;]*/gi, ');');

  // ON UPDATE CURRENT_TIMESTAMP → 去掉
  sql = sql.replace(/\s+ON UPDATE CURRENT_TIMESTAMP/gi, '');

  // 数据类型转换
  sql = sql.replace(/\bMEDIUMTEXT\b/gi, 'TEXT');
  sql = sql.replace(/\bLONGTEXT\b/gi, 'TEXT');
  sql = sql.replace(/\bINT\(\d+\)/gi, 'INT');
  sql = sql.replace(/\bBIGINT\(\d+\)/gi, 'BIGINT');
  sql = sql.replace(/\bTINYINT\(\d+\)/gi, 'TINYINT');

  // 去掉 source 命令行
  sql = sql.replace(/^source\s+.*$/gim, '');

  // 处理 ALTER TABLE ... AFTER xxx（H2不支持AFTER）
  sql = sql.replace(/(ALTER TABLE\s+\w+\s+ADD COLUMN[^;]*?)\s+AFTER\s+`?\w+`?\s*;/gi, '$1;');

  // 去掉 DELIMITER 块和存储过程（H2不支持）
  sql = sql.replace(/DELIMITER\s*\/\/[\s\S]*?DELIMITER\s*;/gi, '');
  sql = sql.replace(/DROP PROCEDURE IF EXISTS[^;]*;/gi, '');
  sql = sql.replace(/CREATE PROCEDURE[\s\S]*?END\s*\/\/?/gi, '');
  sql = sql.replace(/CALL \w+\([^)]*\)\s*;/gi, '');
  sql = sql.replace(/SET @sql[^;]*;/gi, '');
  sql = sql.replace(/PREPARE[^;]*;/gi, '');
  sql = sql.replace(/EXECUTE[^;]*;/gi, '');
  sql = sql.replace(/DEALLOCATE[^;]*;/gi, '');

  // 提取 ALTER 语句（延后执行，放在所有建表之后）
  const alters = [];
  sql = sql.replace(/^\s*(ALTER TABLE[^;]+;)\s*$/gim, (m, alter) => {
    alters.push(alter);
    return '';
  });

  // 提取 inline INDEX/KEY → 单独 CREATE INDEX（支持嵌套括号如 subjects(100)）
  const tableName = filename.replace('.sql', '');
  const indexes = [];
  const indexRegex = /^\s*(UNIQUE\s+)?(?:KEY|INDEX)\s+(\w+)\s*\(((?:[^()]|\([^()]*\))+)\)\s*,?\s*$/gim;
  let m;
  while ((m = indexRegex.exec(sql)) !== null) {
    const unique = m[1] ? 'UNIQUE ' : '';
    const name = m[2];
    let cols = m[3].replace(/\(\d+\)/g, ''); // 去掉前缀长度
    indexes.push(`CREATE ${unique}INDEX IF NOT EXISTS ${name} ON ${tableName} (${cols});`);
  }
  // 从表定义中删除 INDEX/KEY 行（支持嵌套括号）
  sql = sql.replace(/^\s*(UNIQUE\s+)?(?:KEY|INDEX)\s+\w+\s*\((?:[^()]|\([^()]*\))+\)\s*,?\s*$/gim, '');

  // 清理多余的逗号和分号
  sql = sql.replace(/,(\s*\))/g, '$1');
  sql = sql.replace(/;;+/g, ';');

  // 统一 CREATE TABLE IF NOT EXISTS
  sql = sql.replace(/CREATE TABLE (?!IF NOT EXISTS)/gi, 'CREATE TABLE IF NOT EXISTS ');

  return { ddl: sql.trim(), indexes, alters };
}

let schema = `-- H2 Schema (auto-generated)\n`;
let allAlters = [];

for (const file of tableFiles) {
  const raw = readFileSync(join(SQL_DIR, file), 'utf8');
  const { ddl, indexes, alters } = convertDDL(raw, file);
  if (ddl.length < 20) continue;
  if (alters && alters.length) allAlters.push(...alters);
  const tableName = file.replace('.sql', '');
  schema += `\nDROP TABLE IF EXISTS ${tableName};\n`;
  // 确保 DDL 以分号结尾
  let d = ddl;
  if (!d.trimEnd().endsWith(';')) d = d.trimEnd() + ';';
  schema += d + '\n';
  if (indexes.length) schema += indexes.join('\n') + '\n';
}

// ALTER 语句放在最后（所有表建完后执行）
if (allAlters.length) {
  schema += `\n-- === ALTER statements (run after all tables created) ===\n`;
  schema += allAlters.join('\n') + '\n';
}

// 写 schema（Node.js 写 UTF-8 无 BOM）
writeFileSync(join(OUT_DIR, 'init-h2-schema.sql'), schema, 'utf8');
console.log(`Schema written: ${schema.split('\n').length} lines`);

// ========== 种子数据转换 ==========
const seedFiles = [
  'seed_data_subject_knowledge.sql',
  'seed_data_comprehensive_resources.sql',
  'seed_data_exercise.sql',
  'seed_data_micro_course.sql',
  'seed_data_paper_audio.sql',
  'init_test_data.sql',
];

let seed = `-- H2 Seed Data (auto-generated)\n`;
for (const file of seedFiles) {
  try {
    let c = readFileSync(join(SQL_DIR, file), 'utf8');
    c = c.replace(/SET NAMES[^;]*;/gi, '');
    c = c.replace(/^source\s+.*$/gim, '');
    c = c.replace(/USE\s+\w+\s*;/gi, '');
    c = c.replace(/`/g, '');
    // ON DUPLICATE KEY UPDATE → 去掉
    c = c.replace(/\s+ON DUPLICATE KEY UPDATE[^;]*(?=;)/gi, '');
    seed += `\n-- === ${file} ===\n${c}\n`;
    console.log(`Seed: ${file} (${c.split('\n').length} lines)`);
  } catch {
    console.log(`Skip: ${file}`);
  }
}

writeFileSync(join(OUT_DIR, 'seed-h2-data.sql'), seed, 'utf8');
console.log(`Seed written: ${seed.split('\n').length} lines`);
console.log('Done!');
