-- 用户模块数据库表结构
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `multishop_user` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `multishop_user`;

-- 1. 用户基础信息表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    `salt` VARCHAR(32) NOT NULL COMMENT '密码盐值',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `gender` TINYINT DEFAULT 0 COMMENT '性别:0-未知,1-男,2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0-禁用,1-正常,2-锁定',
    `member_level` TINYINT NOT NULL DEFAULT 1 COMMENT '会员等级:1-普通,2-银牌,3-金牌,4-钻石',
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
    `total_points` INT NOT NULL DEFAULT 0 COMMENT '累计积分',
    `available_points` INT NOT NULL DEFAULT 0 COMMENT '可用积分',
    `register_source` VARCHAR(20) DEFAULT 'web' COMMENT '注册来源:web,app,wechat,alipay',
    `register_ip` VARCHAR(45) DEFAULT NULL COMMENT '注册IP',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45) DEFAULT NULL COMMENT '最后登录IP',
    `login_count` INT NOT NULL DEFAULT 0 COMMENT '登录次数',
    `is_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '是否实名认证:0-否,1-是',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号(乐观锁)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`),
    UNIQUE KEY `uk_user_email` (`email`),
    UNIQUE KEY `uk_user_phone` (`phone`),
    KEY `idx_user_status` (`status`),
    KEY `idx_user_member_level` (`member_level`),
    KEY `idx_user_create_time` (`create_time`),
    KEY `idx_user_login` (`phone`, `password`),
    KEY `idx_user_email_login` (`email`, `password`),
    KEY `idx_user_register_source` (`register_source`),
    KEY `idx_user_last_login` (`last_login_time`),
    KEY `idx_user_composite` (`status`, `member_level`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 2. 用户详细资料表
CREATE TABLE IF NOT EXISTS `user_profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `profession` VARCHAR(100) DEFAULT NULL COMMENT '职业',
    `company` VARCHAR(200) DEFAULT NULL COMMENT '公司',
    `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
    `income_level` VARCHAR(20) DEFAULT NULL COMMENT '收入水平',
    `marital_status` TINYINT DEFAULT 0 COMMENT '婚姻状况:0-未知,1-未婚,2-已婚,3-离异',
    `hobby` VARCHAR(500) DEFAULT NULL COMMENT '兴趣爱好',
    `bio` TEXT COMMENT '个人简介',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_profile_user_id` (`user_id`),
    KEY `idx_profile_real_name` (`real_name`),
    KEY `idx_profile_location` (`province`, `city`),
    CONSTRAINT `fk_profile_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户详细资料表';

-- 3. 用户收货地址表
CREATE TABLE IF NOT EXISTS `user_address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `street` VARCHAR(100) DEFAULT NULL COMMENT '街道',
    `detail_address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `postal_code` VARCHAR(10) DEFAULT NULL COMMENT '邮政编码',
    `address_tag` VARCHAR(20) DEFAULT NULL COMMENT '地址标签:家,公司,学校等',
    `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址:0-否,1-是',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_address_user_id` (`user_id`),
    KEY `idx_address_default` (`user_id`, `is_default`),
    KEY `idx_address_location` (`province`, `city`, `district`),
    KEY `idx_address_tag` (`address_tag`),
    CONSTRAINT `fk_address_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收货地址表';

-- 4. 用户积分记录表
CREATE TABLE IF NOT EXISTS `user_points_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID',
    `points_type` TINYINT NOT NULL COMMENT '积分类型:1-获得,2-消费,3-过期,4-退还',
    `points_amount` INT NOT NULL COMMENT '积分数量',
    `points_balance` INT NOT NULL COMMENT '积分余额',
    `source_type` VARCHAR(20) NOT NULL COMMENT '来源类型:order,sign,activity,refund等',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源ID',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_points_user_id` (`user_id`),
    KEY `idx_points_type` (`points_type`),
    KEY `idx_points_source` (`source_type`, `source_id`),
    KEY `idx_points_create_time` (`create_time`),
    KEY `idx_points_expire` (`expire_time`),
    CONSTRAINT `fk_points_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户积分记录表';

-- 5. 用户Token表
CREATE TABLE IF NOT EXISTS `user_token` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `token` VARCHAR(500) NOT NULL COMMENT 'JWT Token',
    `refresh_token` VARCHAR(500) NOT NULL COMMENT '刷新Token',
    `device_type` VARCHAR(20) DEFAULT NULL COMMENT '设备类型:web,app,wechat',
    `device_id` VARCHAR(100) DEFAULT NULL COMMENT '设备ID',
    `ip_address` VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `expire_time` DATETIME NOT NULL COMMENT 'Token过期时间',
    `refresh_expire_time` DATETIME NOT NULL COMMENT '刷新Token过期时间',
    `is_active` TINYINT NOT NULL DEFAULT 1 COMMENT '是否活跃:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_token` (`token`),
    UNIQUE KEY `uk_refresh_token` (`refresh_token`),
    KEY `idx_token_user_id` (`user_id`),
    KEY `idx_token_device` (`device_type`, `device_id`),
    KEY `idx_token_expire` (`expire_time`),
    KEY `idx_token_active` (`is_active`),
    CONSTRAINT `fk_token_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户Token表';

-- 6. 会员等级配置表
CREATE TABLE IF NOT EXISTS `member_level_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `level` TINYINT NOT NULL COMMENT '等级:1-普通,2-银牌,3-金牌,4-钻石',
    `level_name` VARCHAR(20) NOT NULL COMMENT '等级名称',
    `min_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '最低消费金额',
    `max_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '最高消费金额',
    `discount_rate` DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '折扣率',
    `points_rate` DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '积分倍率',
    `privileges` JSON DEFAULT NULL COMMENT '特权配置',
    `is_active` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level` (`level`),
    KEY `idx_level_amount` (`min_amount`, `max_amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员等级配置表';

-- 7. 用户登录日志表
CREATE TABLE IF NOT EXISTS `user_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `login_type` VARCHAR(20) NOT NULL COMMENT '登录类型:password,sms,wechat,alipay',
    `login_ip` VARCHAR(45) DEFAULT NULL COMMENT '登录IP',
    `login_location` VARCHAR(100) DEFAULT NULL COMMENT '登录地点',
    `device_type` VARCHAR(20) DEFAULT NULL COMMENT '设备类型',
    `device_info` VARCHAR(500) DEFAULT NULL COMMENT '设备信息',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `login_status` TINYINT NOT NULL COMMENT '登录状态:0-失败,1-成功',
    `fail_reason` VARCHAR(100) DEFAULT NULL COMMENT '失败原因',
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (`id`),
    KEY `idx_login_user_id` (`user_id`),
    KEY `idx_login_time` (`login_time`),
    KEY `idx_login_status` (`login_status`),
    KEY `idx_login_ip` (`login_ip`),
    CONSTRAINT `fk_login_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录日志表';

-- 初始化会员等级配置数据
INSERT INTO `member_level_config` (`level`, `level_name`, `min_amount`, `max_amount`, `discount_rate`, `points_rate`, `privileges`) VALUES
(1, '普通会员', 0.00, 999.99, 0.95, 1.00, '{"free_shipping_threshold": 99, "birthday_discount": 0.05}'),
(2, '银牌会员', 1000.00, 4999.99, 0.90, 1.20, '{"free_shipping_threshold": 0, "birthday_discount": 0.10, "priority_service": true}'),
(3, '金牌会员', 5000.00, 19999.99, 0.85, 1.50, '{"free_shipping_threshold": 0, "birthday_discount": 0.15, "priority_service": true, "exclusive_products": true}'),
(4, '钻石会员', 20000.00, NULL, 0.80, 2.00, '{"free_shipping_threshold": 0, "birthday_discount": 0.20, "priority_service": true, "exclusive_products": true, "personal_advisor": true}');