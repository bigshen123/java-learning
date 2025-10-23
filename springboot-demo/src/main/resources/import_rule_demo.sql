-- 仅用于测试


drop table import_rule;
-- Rules table
CREATE TABLE IF NOT EXISTS import_rule (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  rule_code VARCHAR(64) NOT NULL,
  field_name VARCHAR(128) NOT NULL,
  rule_type VARCHAR(32) NOT NULL,
  rule_content TEXT,
  error_msg VARCHAR(255),
  enabled BOOLEAN DEFAULT TRUE
);

-- Target table
CREATE TABLE IF NOT EXISTS target_table (
  id VARCHAR(64),
  name VARCHAR(128),
  age INT,
  phone VARCHAR(32),
  email VARCHAR(128)
);



-- Example rules
INSERT INTO import_rule (rule_code, field_name, rule_type, rule_content, error_msg, enabled) VALUES
('NOT_NULL_NAME', 'name', 'NOT_NULL', NULL, '姓名不能为空', TRUE),
('PHONE_REGEX', 'phone', 'REGEX', '^\\d{11}$', '手机号必须为11位数字', TRUE),
('AGE_RANGE', 'age', 'RANGE', '18~60', '年龄需在18-60之间', TRUE),
('EMAIL_CHECK', 'email', 'REGEX', '^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$', '邮箱格式不正确', TRUE);

