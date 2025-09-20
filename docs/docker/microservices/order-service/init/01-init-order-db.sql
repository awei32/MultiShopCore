-- 创建订单服务数据库
CREATE DATABASE IF NOT EXISTS `order_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `order_service_db`;

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order_info` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    `pay_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实付金额',
    `freight_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '运费',
    `discount_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
    `order_status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '订单状态:1待付款,2待发货,3待收货,4已完成,5已取消',
    `pay_status` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '支付状态:0未支付,1已支付,2已退款',
    `pay_type` TINYINT(2) DEFAULT NULL COMMENT '支付方式:1微信,2支付宝,3银联',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `delivery_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `receive_time` DATETIME DEFAULT NULL COMMENT '收货时间',
    `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
    `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_status` (`order_status`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_pay_time` (`pay_time`),
    KEY `idx_user_status` (`user_id`, `order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- 创建订单详情表
CREATE TABLE IF NOT EXISTS `order_detail` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    `quantity` INT(11) NOT NULL COMMENT '购买数量',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    `sku_id` BIGINT(20) DEFAULT NULL COMMENT 'SKU ID',
    `sku_name` VARCHAR(255) DEFAULT NULL COMMENT 'SKU名称',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 创建收货地址表
CREATE TABLE IF NOT EXISTS `order_address` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `receiver_name` VARCHAR(100) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `postal_code` VARCHAR(10) DEFAULT NULL COMMENT '邮政编码',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';

-- 创建订单日志表
CREATE TABLE IF NOT EXISTS `order_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `operation_desc` VARCHAR(255) NOT NULL COMMENT '操作描述',
    `operator_id` BIGINT(20) DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(100) DEFAULT NULL COMMENT '操作人姓名',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单操作日志表';

-- 创建购物车表
CREATE TABLE IF NOT EXISTS `shopping_cart` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
    `sku_id` BIGINT(20) DEFAULT NULL COMMENT 'SKU ID',
    `quantity` INT(11) NOT NULL DEFAULT 1 COMMENT '数量',
    `selected` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否选中:0否,1是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product_sku` (`user_id`, `product_id`, `sku_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_selected` (`selected`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 插入测试数据
INSERT INTO `order_info` (`order_no`, `user_id`, `total_amount`, `pay_amount`, `order_status`, `pay_status`) VALUES
('ORD202401010001', 1, 299.00, 299.00, 4, 1),
('ORD202401010002', 2, 599.00, 599.00, 2, 1),
('ORD202401010003', 1, 199.00, 199.00, 1, 0);

INSERT INTO `order_detail` (`order_id`, `product_id`, `product_name`, `product_price`, `quantity`, `total_price`) VALUES
(1, 1001, '测试商品1', 299.00, 1, 299.00),
(2, 1002, '测试商品2', 299.00, 2, 598.00),
(3, 1003, '测试商品3', 199.00, 1, 199.00);

-- 创建主从复制用户
CREATE USER 'replication'@'%' IDENTIFIED BY 'replication123';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'%';
FLUSH PRIVILEGES;