-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS `user_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `user_service_db`;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(255) NOT NULL,
    `avatar` VARCHAR(500),
    `nickname` VARCHAR(100),
    `gender` TINYINT(1) DEFAULT 0 COMMENT '0:未知,1:男,2:女',
    `birthday` DATE,
    `status` TINYINT(1) DEFAULT 1 COMMENT '0:禁用,1:启用',
    `last_login_time` DATETIME,
    `last_login_ip` VARCHAR(50),
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_update_time` (`update_time`),
    KEY `idx_username_password` (`username`, `password`),
    KEY `idx_email_password` (`email`, `password`),
    KEY `idx_phone_password` (`phone`, `password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基础信息表';

-- 创建用户详情表
CREATE TABLE IF NOT EXISTS `user_profile` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `real_name` VARCHAR(100),
    `id_card` VARCHAR(18),
    `address` VARCHAR(500),
    `company` VARCHAR(200),
    `profession` VARCHAR(100),
    `bio` TEXT,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_real_name` (`real_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详情表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `code` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255),
    `status` TINYINT(1) DEFAULT 1 COMMENT '0:禁用,1:启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `role_id` BIGINT(20) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 创建用户登录日志表
CREATE TABLE IF NOT EXISTS `user_login_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `login_ip` VARCHAR(50),
    `login_location` VARCHAR(200),
    `browser` VARCHAR(100),
    `os` VARCHAR(100),
    `device` VARCHAR(100),
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `status` TINYINT(1) DEFAULT 1 COMMENT '0:失败,1:成功',
    `message` VARCHAR(255),
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_time` (`login_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录日志表';

-- 插入默认角色
INSERT INTO `role` (`name`, `code`, `description`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员'),
('管理员', 'ADMIN', '系统管理员'),
('普通用户', 'USER', '普通用户'),
('VIP用户', 'VIP_USER', 'VIP用户');

-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@multishop.com', '13800138000', '系统管理员'),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'test@multishop.com', '13800138001', '测试用户');

-- 为用户分配角色
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),  -- admin -> SUPER_ADMIN
(2, 3);  -- test -> USER

-- 创建主从复制用户
CREATE USER 'replication'@'%' IDENTIFIED BY 'replication123';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'%';
FLUSH PRIVILEGES;