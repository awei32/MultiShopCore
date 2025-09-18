CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(255) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    -- 时间范围查询索引
    KEY `idx_create_time` (`create_time`),
    KEY `idx_update_time` (`update_time`),
    -- 复合索引：用于登录验证
    KEY `idx_username_password` (`username`, `password`),
    KEY `idx_email_password` (`email`, `password`),
    KEY `idx_phone_password` (`phone`, `password`),
    -- 时间排序复合索引
    KEY `idx_create_time_id` (`create_time`, `id`),
    KEY `idx_update_time_id` (`update_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;