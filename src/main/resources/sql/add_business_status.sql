-- 为merchant表添加business_status字段
ALTER TABLE merchant ADD COLUMN business_status INT DEFAULT 1 COMMENT '营业状态：1-营业中，0-休息中';