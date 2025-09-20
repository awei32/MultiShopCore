# MultiShop Core 微服务数据库集群部署

本目录包含了 MultiShop Core 项目的微服务数据库集群部署配置，每个微服务都有独立的MySQL集群，实现数据隔离和高可用。

## 架构概述

### 微服务数据库分离架构
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   用户服务集群    │    │   订单服务集群    │    │   商品服务集群    │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ user-mysql-master│    │order-mysql-master│    │product-mysql-master│
│ user-mysql-slave1│    │order-mysql-slave1│    │product-mysql-slave1│
│ user-mysql-slave2│    │order-mysql-slave2│    │product-mysql-slave2│
│ user-proxysql    │    │order-proxysql    │    │product-proxysql    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
     端口: 3306-3308         端口: 3316-3318         端口: 3326-3328
     ProxySQL: 6033         ProxySQL: 6043         ProxySQL: 6053
```

### 每个集群包含
- **1个主数据库**: 处理写操作
- **2个从数据库**: 处理读操作，提供高可用
- **1个ProxySQL**: 负载均衡和读写分离

## 目录结构

```
microservices/
├── start-all-services.sh          # 统一启动脚本
├── setup-replication.sh           # 主从复制配置脚本
├── README.md                       # 说明文档
├── user-service/                   # 用户服务数据库集群
│   ├── docker-compose.yml
│   ├── conf/
│   │   ├── master.cnf
│   │   └── slave.cnf
│   ├── init/
│   │   └── 01-init-user-db.sql
│   ├── proxysql/
│   │   └── proxysql.cnf
│   └── logs/
├── order-service/                  # 订单服务数据库集群
│   ├── docker-compose.yml
│   ├── conf/
│   ├── init/
│   ├── proxysql/
│   └── logs/
└── product-service/                # 商品服务数据库集群
    ├── docker-compose.yml
    ├── conf/
    ├── init/
    ├── proxysql/
    └── logs/
```

## 快速开始

### 1. 给脚本执行权限
```bash
chmod +x start-all-services.sh setup-replication.sh
```

### 2. 启动所有微服务数据库集群
```bash
./start-all-services.sh
# 选择选项 1 启动所有服务
```

### 3. 配置主从复制
```bash
./start-all-services.sh
# 选择选项 7 配置主从复制
```

## 服务详情

### 用户服务数据库集群

**连接信息:**
- 主库: `localhost:3306`
- 从库1: `localhost:3307`
- 从库2: `localhost:3308`
- ProxySQL: `localhost:6033`
- 数据库: `user_service_db`
- 用户名/密码: `user_service/userdb123`

**包含表:**
- `user`: 用户基础信息
- `user_profile`: 用户详情
- `role`: 角色信息
- `user_role`: 用户角色关联
- `user_login_log`: 登录日志

### 订单服务数据库集群

**连接信息:**
- 主库: `localhost:3316`
- 从库1: `localhost:3317`
- 从库2: `localhost:3318`
- ProxySQL: `localhost:6043`
- 数据库: `order_service_db`
- 用户名/密码: `order_service/orderdb123`

**包含表:**
- `order_info`: 订单信息
- `order_detail`: 订单详情
- `order_address`: 收货地址
- `order_log`: 订单日志
- `shopping_cart`: 购物车

### 商品服务数据库集群

**连接信息:**
- 主库: `localhost:3326`
- 从库1: `localhost:3327`
- 从库2: `localhost:3328`
- ProxySQL: `localhost:6053`
- 数据库: `product_service_db`
- 用户名/密码: `product_service/productdb123`

**包含表:**
- `category`: 商品分类
- `brand`: 品牌信息
- `product`: 商品信息
- `product_attribute`: 商品属性
- `product_attribute_value`: 商品属性值
- `product_sku`: 商品SKU

## ProxySQL 读写分离

### 连接方式
应用程序通过ProxySQL连接数据库，ProxySQL会自动进行读写分离：

```java
// 用户服务数据源配置
spring.datasource.url=jdbc:mysql://localhost:6033/user_service_db
spring.datasource.username=user_service
spring.datasource.password=userdb123

// 订单服务数据源配置
spring.datasource.url=jdbc:mysql://localhost:6043/order_service_db
spring.datasource.username=order_service
spring.datasource.password=orderdb123

// 商品服务数据源配置
spring.datasource.url=jdbc:mysql://localhost:6053/product_service_db
spring.datasource.username=product_service
spring.datasource.password=productdb123
```

### 路由规则
- **写操作** (INSERT/UPDATE/DELETE): 路由到主库
- **读操作** (SELECT): 路由到从库
- **事务查询** (SELECT FOR UPDATE): 路由到主库

## 常用操作

### 启动单个服务
```bash
# 启动用户服务
cd user-service && docker-compose up -d

# 启动订单服务
cd order-service && docker-compose up -d

# 启动商品服务
cd product-service && docker-compose up -d
```

### 停止服务
```bash
# 停止所有服务
./start-all-services.sh
# 选择选项 5

# 停止单个服务
cd user-service && docker-compose down
```

### 查看日志
```bash
# 查看用户服务主库日志
cd user-service && docker-compose logs -f user-mysql-master

# 查看ProxySQL日志
cd user-service && docker-compose logs -f user-proxysql
```

### 连接数据库
```bash
# 连接用户服务主库
docker exec -it user-mysql-master mysql -uuser_service -puserdb123 user_service_db

# 通过ProxySQL连接
docker exec -it user-proxysql mysql -uuser_service -puserdb123 -h127.0.0.1 -P6033 user_service_db
```

### 检查主从复制状态
```bash
# 检查从库状态
docker exec -it user-mysql-slave1 mysql -uroot -puserdb_root123 -e "SHOW SLAVE STATUS\G"
```

## 监控和维护

### 健康检查
```bash
# 查看所有容器状态
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(mysql|proxysql)"

# 检查ProxySQL状态
docker exec -it user-proxysql mysql -uadmin -padmin123 -h127.0.0.1 -P6032 -e "SELECT * FROM mysql_servers;"
```

### 备份数据库
```bash
# 备份用户服务数据库
docker exec user-mysql-master mysqldump -uroot -puserdb_root123 user_service_db > user_service_backup.sql

# 备份所有服务数据库
for service in user order product; do
    docker exec ${service}-mysql-master mysqldump -uroot -p${service}db_root123 ${service}_service_db > ${service}_service_backup.sql
done
```

### 扩展从库
如需添加更多从库，可以修改对应服务的 `docker-compose.yml` 文件，添加新的从库容器。

## 故障排除

### 1. 端口冲突
如果端口被占用，修改 `docker-compose.yml` 中的端口映射。

### 2. 主从复制失败
```bash
# 重新配置主从复制
./setup-replication.sh user-service

# 手动检查主库状态
docker exec user-mysql-master mysql -uroot -puserdb_root123 -e "SHOW MASTER STATUS;"

# 检查从库错误
docker exec user-mysql-slave1 mysql -uroot -puserdb_root123 -e "SHOW SLAVE STATUS\G" | grep Error
```

### 3. ProxySQL连接问题
```bash
# 检查ProxySQL配置
docker exec user-proxysql mysql -uadmin -padmin123 -h127.0.0.1 -P6032 -e "SELECT * FROM mysql_servers;"

# 重启ProxySQL
cd user-service && docker-compose restart user-proxysql
```

### 4. 数据不一致
```bash
# 检查主从延迟
docker exec user-mysql-slave1 mysql -uroot -puserdb_root123 -e "SHOW SLAVE STATUS\G" | grep Seconds_Behind_Master
```

## 性能优化

### 1. MySQL配置优化
- 已针对每个服务优化了 `my.cnf` 配置
- 根据实际负载调整 `innodb_buffer_pool_size`
- 监控慢查询日志

### 2. ProxySQL优化
- 调整连接池大小
- 优化查询路由规则
- 启用查询缓存

### 3. 硬件建议
- **CPU**: 每个MySQL实例至少2核
- **内存**: 每个MySQL实例至少2GB
- **存储**: 使用SSD存储提高I/O性能

## 安全建议

1. **修改默认密码**: 生产环境必须修改所有默认密码
2. **网络隔离**: 使用Docker网络隔离不同服务
3. **SSL连接**: 启用MySQL SSL连接
4. **定期备份**: 建立自动备份策略
5. **监控告警**: 部署监控系统监控数据库状态

## 扩展说明

### 添加新的微服务数据库集群

1. 复制现有服务目录结构
2. 修改端口配置避免冲突
3. 更新数据库名称和用户信息
4. 创建对应的初始化SQL脚本
5. 更新统一启动脚本