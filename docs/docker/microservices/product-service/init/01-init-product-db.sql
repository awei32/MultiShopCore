-- 创建商品服务数据库
CREATE DATABASE IF NOT EXISTS `product_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `product_service_db`;

-- 创建商品分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    `level` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '分类层级',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '分类图片',
    `description` TEXT COMMENT '分类描述',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态:0禁用,1启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_level` (`level`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 创建品牌表
CREATE TABLE IF NOT EXISTS `brand` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '品牌名称',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT '品牌Logo',
    `description` TEXT COMMENT '品牌描述',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态:0禁用,1启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- 创建商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
    `subtitle` VARCHAR(255) DEFAULT NULL COMMENT '商品副标题',
    `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
    `brand_id` BIGINT(20) DEFAULT NULL COMMENT '品牌ID',
    `product_sn` VARCHAR(100) NOT NULL COMMENT '商品编号',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `cost_price` DECIMAL(10,2) DEFAULT NULL COMMENT '成本价',
    `stock` INT(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
    `low_stock` INT(11) DEFAULT 0 COMMENT '库存预警值',
    `unit` VARCHAR(20) DEFAULT '件' COMMENT '单位',
    `weight` DECIMAL(10,2) DEFAULT NULL COMMENT '重量(kg)',
    `volume` DECIMAL(10,2) DEFAULT NULL COMMENT '体积(m³)',
    `main_image` VARCHAR(500) DEFAULT NULL COMMENT '主图',
    `images` TEXT COMMENT '商品图片，JSON格式',
    `detail` LONGTEXT COMMENT '商品详情',
    `keywords` VARCHAR(255) DEFAULT NULL COMMENT '关键词',
    `note` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `album_pics` TEXT COMMENT '画册图片，JSON格式',
    `detail_title` VARCHAR(255) DEFAULT NULL COMMENT '详情标题',
    `detail_desc` TEXT COMMENT '详情描述',
    `delete_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除状态:0未删除,1已删除',
    `publish_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '上架状态:0下架,1上架',
    `new_status` TINYINT(1) DEFAULT 0 COMMENT '新品状态:0不是新品,1新品',
    `recommend_status` TINYINT(1) DEFAULT 0 COMMENT '推荐状态:0不推荐,1推荐',
    `verify_status` TINYINT(1) DEFAULT 0 COMMENT '审核状态:0未审核,1审核通过',
    `sort` INT(11) DEFAULT 0 COMMENT '排序',
    `sale` INT(11) DEFAULT 0 COMMENT '销量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_sn` (`product_sn`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_brand_id` (`brand_id`),
    KEY `idx_publish_status` (`publish_status`),
    KEY `idx_delete_status` (`delete_status`),
    KEY `idx_price` (`price`),
    KEY `idx_sale` (`sale`),
    KEY `idx_sort` (`sort`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- 创建商品属性表
CREATE TABLE IF NOT EXISTS `product_attribute` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '属性名称',
    `select_type` TINYINT(1) DEFAULT 0 COMMENT '属性选择类型:0唯一,1单选,2多选',
    `input_type` TINYINT(1) DEFAULT 0 COMMENT '属性录入方式:0手工录入,1从列表中选取',
    `input_list` TEXT COMMENT '可选值列表，以逗号隔开',
    `sort` INT(11) DEFAULT 0 COMMENT '排序字段',
    `filter_type` TINYINT(1) DEFAULT 0 COMMENT '分类筛选样式:0普通,1颜色',
    `search_type` TINYINT(1) DEFAULT 0 COMMENT '检索类型:0不需要检索,1关键字检索,2范围检索',
    `related_status` TINYINT(1) DEFAULT 0 COMMENT '相同属性产品是否关联:0不关联,1关联',
    `hand_add_status` TINYINT(1) DEFAULT 0 COMMENT '是否支持手动新增:0不支持,1支持',
    `type` TINYINT(1) DEFAULT 0 COMMENT '属性类型:0规格,1参数',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- 创建商品属性值表
CREATE TABLE IF NOT EXISTS `product_attribute_value` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
    `product_attribute_id` BIGINT(20) NOT NULL COMMENT '商品属性ID',
    `value` VARCHAR(255) DEFAULT NULL COMMENT '手动添加规格或参数的值，参数单值，规格有多个时以逗号隔开',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_product_attribute_id` (`product_attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性值表';

-- 创建商品SKU表
CREATE TABLE IF NOT EXISTS `product_sku` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
    `sku_code` VARCHAR(100) NOT NULL COMMENT 'SKU编码',
    `price` DECIMAL(10,2) NOT NULL COMMENT 'SKU价格',
    `stock` INT(11) NOT NULL DEFAULT 0 COMMENT 'SKU库存',
    `low_stock` INT(11) DEFAULT 0 COMMENT 'SKU库存预警值',
    `pic` VARCHAR(500) DEFAULT NULL COMMENT 'SKU图片',
    `sale` INT(11) DEFAULT 0 COMMENT 'SKU销量',
    `sp_data` TEXT COMMENT 'SKU属性值，JSON格式',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_code` (`sku_code`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_price` (`price`),
    KEY `idx_stock` (`stock`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 插入测试数据
INSERT INTO `category` (`name`, `parent_id`, `level`) VALUES
('电子产品', 0, 1),
('手机数码', 1, 2),
('电脑办公', 1, 2),
('服装鞋帽', 0, 1),
('男装', 4, 2),
('女装', 4, 2);

INSERT INTO `brand` (`name`, `description`) VALUES
('苹果', 'Apple Inc.'),
('华为', 'HUAWEI Technologies Co., Ltd.'),
('小米', 'Xiaomi Corporation'),
('耐克', 'NIKE, Inc.');

INSERT INTO `product` (`name`, `category_id`, `brand_id`, `product_sn`, `price`, `stock`, `publish_status`) VALUES
('iPhone 15 Pro', 2, 1, 'IP15PRO001', 7999.00, 100, 1),
('华为Mate60 Pro', 2, 2, 'HWM60PRO001', 6999.00, 150, 1),
('小米14 Ultra', 2, 3, 'XM14ULTRA001', 5999.00, 200, 1);

-- 创建主从复制用户
CREATE USER 'replication'@'%' IDENTIFIED BY 'replication123';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'%';
FLUSH PRIVILEGES;