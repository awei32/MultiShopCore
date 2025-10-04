-- =============================================
-- 用户模块数据库建表语句
-- 基于实体类定义，确保与Java实体类完全一致
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `multishop_user` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `multishop_user`;

-- =============================================
-- 1. 用户基础信息表 (User)
-- =============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `gender` TINYINT DEFAULT NULL COMMENT '性别:0-未知,1-男,2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0-禁用,1-正常,2-锁定',
    `member_level` TINYINT NOT NULL DEFAULT 1 COMMENT '会员等级:1-普通,2-银牌,3-金牌,4-钻石',
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
    `total_points` INT NOT NULL DEFAULT 0 COMMENT '累计积分',
    `available_points` INT NOT NULL DEFAULT 0 COMMENT '可用积分',
    `register_source` TINYINT NOT NULL DEFAULT 1 COMMENT '注册来源:1-网站,2-APP,3-微信,4-支付宝',
    `register_ip` VARCHAR(45) DEFAULT NULL COMMENT '注册IP',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45) DEFAULT NULL COMMENT '最后登录IP',
    `login_count` INT NOT NULL DEFAULT 0 COMMENT '登录次数',
    `is_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已验证:0-否,1-是',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_member_level` (`member_level`),
    KEY `idx_register_source` (`register_source`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- =============================================
-- 2. 用户详细资料表 (UserProfile)
-- =============================================
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
    `id` BIGINT NOT NULL COMMENT '资料ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `profession` VARCHAR(100) DEFAULT NULL COMMENT '职业',
    `company` VARCHAR(200) DEFAULT NULL COMMENT '公司',
    `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
    `income_level` VARCHAR(50) DEFAULT NULL COMMENT '收入水平',
    `marital_status` TINYINT DEFAULT NULL COMMENT '婚姻状况:1-未婚,2-已婚,3-离异,4-丧偶',
    `hobby` VARCHAR(500) DEFAULT NULL COMMENT '兴趣爱好',
    `bio` TEXT DEFAULT NULL COMMENT '个人简介',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_real_name` (`real_name`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户详细资料表';

-- =============================================
-- 3. 用户收货地址表 (UserAddress)
-- =============================================
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
    `id` BIGINT NOT NULL COMMENT '地址ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `street` VARCHAR(100) DEFAULT NULL COMMENT '街道',
    `detail_address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `postal_code` VARCHAR(10) DEFAULT NULL COMMENT '邮政编码',
    `address_tag` VARCHAR(20) DEFAULT NULL COMMENT '地址标签',
    `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认:0-否,1-是',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_default` (`is_default`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收货地址表';

-- =============================================
-- 4. 用户积分记录表 (UserPointsLog)
-- =============================================
DROP TABLE IF EXISTS `user_points_log`;
CREATE TABLE `user_points_log` (
    `id` BIGINT NOT NULL COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `order_id` BIGINT DEFAULT NULL COMMENT '订单ID',
    `points_type` TINYINT NOT NULL COMMENT '积分类型:1-获得,2-消费,3-过期,4-退还',
    `points_amount` INT NOT NULL COMMENT '积分数量',
    `points_balance` INT NOT NULL COMMENT '积分余额',
    `source_type` TINYINT NOT NULL COMMENT '来源类型:1-注册,2-购物,3-签到,4-活动,5-推荐,6-兑换,7-退款',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源ID',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_points_type` (`points_type`),
    KEY `idx_source_type` (`source_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户积分记录表';

-- =============================================
-- 5. 用户会员等级配置表 (UserMemberLevel)
-- =============================================
DROP TABLE IF EXISTS `user_member_level`;
CREATE TABLE `user_member_level` (
    `id` BIGINT NOT NULL COMMENT '等级ID',
    `level_code` VARCHAR(20) NOT NULL COMMENT '等级编码',
    `level_name` VARCHAR(50) NOT NULL COMMENT '等级名称',
    `min_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '最低消费金额',
    `max_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '最高消费金额',
    `discount_rate` DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '折扣率',
    `points_rate` DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '积分倍率',
    `free_shipping` TINYINT NOT NULL DEFAULT 0 COMMENT '是否包邮:0-否,1-是',
    `birthday_discount` DECIMAL(3,2) DEFAULT NULL COMMENT '生日折扣',
    `level_icon` VARCHAR(200) DEFAULT NULL COMMENT '等级图标',
    `level_color` VARCHAR(20) DEFAULT NULL COMMENT '等级颜色',
    `privileges` TEXT DEFAULT NULL COMMENT '特权描述',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `is_active` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用:0-否,1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level_code` (`level_code`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会员等级配置表';

-- =============================================
-- 6. 用户登录日志表 (UserLoginLog)
-- =============================================
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log` (
    `id` BIGINT NOT NULL COMMENT '日志ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `login_type` TINYINT NOT NULL COMMENT '登录类型:1-密码,2-短信,3-微信,4-支付宝,5-QQ',
    `login_device` VARCHAR(50) DEFAULT NULL COMMENT '登录设备',
    `login_ip` VARCHAR(45) NOT NULL COMMENT '登录IP',
    `login_location` VARCHAR(100) DEFAULT NULL COMMENT '登录地点',
    `user_agent` TEXT DEFAULT NULL COMMENT '用户代理',
    `login_status` TINYINT NOT NULL COMMENT '登录状态:1-成功,2-失败',
    `fail_reason` VARCHAR(200) DEFAULT NULL COMMENT '失败原因',
    `session_id` VARCHAR(100) DEFAULT NULL COMMENT '会话ID',
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `logout_time` DATETIME DEFAULT NULL COMMENT '登出时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_type` (`login_type`),
    KEY `idx_login_status` (`login_status`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录日志表';

-- =============================================
-- 7. 用户消息表 (UserMessage)
-- =============================================
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
    `id` BIGINT NOT NULL COMMENT '消息ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `message_type` TINYINT NOT NULL COMMENT '消息类型:1-系统消息,2-订单消息,3-活动消息,4-客服消息',
    `title` VARCHAR(100) NOT NULL COMMENT '消息标题',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `source` VARCHAR(50) DEFAULT NULL COMMENT '消息来源',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID(订单ID、活动ID等)',
    `jump_url` VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `icon` VARCHAR(200) DEFAULT NULL COMMENT '消息图标',
    `read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读:0-未读,1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0-否,1-是',
    `delete_time` DATETIME DEFAULT NULL COMMENT '删除时间',
    `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_message_type` (`message_type`),
    KEY `idx_read` (`read`),
    KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户消息表';

-- =============================================
-- 8. 用户第三方授权表 (UserOauth)
-- =============================================
DROP TABLE IF EXISTS `user_oauth`;
CREATE TABLE `user_oauth` (
    `id` BIGINT NOT NULL COMMENT '绑定ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `oauth_type` VARCHAR(20) NOT NULL COMMENT '第三方类型:wechat,alipay,qq,weibo',
    `oauth_id` VARCHAR(100) NOT NULL COMMENT '第三方用户ID',
    `oauth_name` VARCHAR(100) DEFAULT NULL COMMENT '第三方用户名',
    `oauth_avatar` VARCHAR(500) DEFAULT NULL COMMENT '第三方头像',
    `access_token` VARCHAR(500) DEFAULT NULL COMMENT '访问令牌',
    `refresh_token` VARCHAR(500) DEFAULT NULL COMMENT '刷新令牌',
    `expires_in` INT DEFAULT NULL COMMENT '令牌过期时间(秒)',
    `union_id` VARCHAR(100) DEFAULT NULL COMMENT '开放平台统一ID',
    `extra_info` TEXT DEFAULT NULL COMMENT '额外信息(JSON格式)',
    `bind_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_oauth_type_id` (`oauth_type`, `oauth_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_oauth_type` (`oauth_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户第三方授权表';

-- =============================================
-- 9. 用户偏好设置表 (UserPreference)
-- =============================================
DROP TABLE IF EXISTS `user_preference`;
CREATE TABLE `user_preference` (
    `id` BIGINT NOT NULL COMMENT '偏好ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `language` VARCHAR(10) DEFAULT 'zh-CN' COMMENT '语言偏好',
    `timezone` VARCHAR(50) DEFAULT 'Asia/Shanghai' COMMENT '时区',
    `currency` VARCHAR(10) DEFAULT 'CNY' COMMENT '货币偏好',
    `theme` VARCHAR(20) DEFAULT 'light' COMMENT '主题偏好:light,dark,auto',
    `email_notification` TINYINT NOT NULL DEFAULT 1 COMMENT '是否接收邮件通知:0-否,1-是',
    `sms_notification` TINYINT NOT NULL DEFAULT 1 COMMENT '是否接收短信通知:0-否,1-是',
    `push_notification` TINYINT NOT NULL DEFAULT 1 COMMENT '是否接收推送通知:0-否,1-是',
    `marketing_notification` TINYINT NOT NULL DEFAULT 0 COMMENT '是否接收营销信息:0-否,1-是',
    `privacy_level` VARCHAR(20) DEFAULT 'public' COMMENT '隐私设置:public,friends,private',
    `show_online_status` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示在线状态:0-否,1-是',
    `allow_search` TINYINT NOT NULL DEFAULT 1 COMMENT '是否允许搜索:0-否,1-是',
    `default_address_id` BIGINT DEFAULT NULL COMMENT '默认收货地址ID',
    `preferred_categories` TEXT DEFAULT NULL COMMENT '偏好商品分类(JSON格式)',
    `preferred_brands` TEXT DEFAULT NULL COMMENT '偏好品牌(JSON格式)',
    `price_range` VARCHAR(100) DEFAULT NULL COMMENT '价格区间偏好(JSON格式)',
    `shopping_habits` TEXT DEFAULT NULL COMMENT '购物习惯(JSON格式)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_language` (`language`),
    KEY `idx_theme` (`theme`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好设置表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入会员等级配置数据
INSERT INTO `user_member_level` (`id`, `level_code`, `level_name`, `min_amount`, `max_amount`, `discount_rate`, `points_rate`, `free_shipping`, `birthday_discount`, `level_icon`, `level_color`, `privileges`, `sort_order`, `is_active`) VALUES
(1, 'BRONZE', '普通会员', 0.00, 999.99, 1.00, 1.00, 0, NULL, '/icons/bronze.png', '#CD7F32', '基础会员权益', 1, 1),
(2, 'SILVER', '银牌会员', 1000.00, 4999.99, 0.98, 1.20, 0, 0.05, '/icons/silver.png', '#C0C0C0', '银牌会员专享折扣', 2, 1),
(3, 'GOLD', '金牌会员', 5000.00, 19999.99, 0.95, 1.50, 1, 0.10, '/icons/gold.png', '#FFD700', '金牌会员免邮特权', 3, 1),
(4, 'DIAMOND', '钻石会员', 20000.00, NULL, 0.90, 2.00, 1, 0.15, '/icons/diamond.png', '#B9F2FF', '钻石会员尊享服务', 4, 1);