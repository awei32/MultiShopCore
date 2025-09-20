# MultiShop Core MySQL Docker 部署

本目录包含了 MultiShop Core 项目的 MySQL 数据库 Docker 部署脚本。

## 文件结构

```
docker/
├── docker-compose.yml          # Docker Compose 配置文件
├── start-mysql.sh             # MySQL 启动脚本
├── stop-mysql.sh              # MySQL 停止脚本
├── README.md                  # 说明文档
├── conf/
│   └── my.cnf                 # MySQL 配置文件
└── init/
    └── 01-init-database.sql   # 数据库初始化脚本
```

## 快速开始

### 1. 启动 MySQL

```bash
# 给脚本执行权限
chmod +x start-mysql.sh stop-mysql.sh

# 启动 MySQL
./start-mysql.sh
```

### 2. 连接信息

- **主机**: localhost
- **端口**: 3306
- **数据库**: multishop_core
- **用户名**: multishop
- **密码**: multishop123
- **Root密码**: root123456

### 3. 测试用户

数据库会自动创建以下测试用户：

- **管理员**: username=`admin`, password=`123456`
- **普通用户**: username=`test`, password=`123456`

注意：密码已使用 BCrypt 加密存储。

## 常用命令

### 查看容器状态
```bash
docker ps
```

### 查看日志
```bash
docker-compose logs -f mysql
```

### 进入容器
```bash
docker exec -it multishop-mysql bash
```

### 连接数据库
```bash
# 使用 multishop 用户连接
docker exec -it multishop-mysql mysql -u multishop -p multishop_core

# 使用 root 用户连接
docker exec -it multishop-mysql mysql -u root -p
```

### 停止服务
```bash
# 停止容器（保留数据）
./stop-mysql.sh

# 或者使用 docker-compose
docker-compose down
```

### 完全清理
```bash
# 停止容器并删除所有数据
docker-compose down -v
```

## 数据库表结构

### user 表
用户基础信息表，包含以下字段：
- `id`: 主键，自增
- `username`: 用户名（唯一）
- `password`: 密码（BCrypt加密）
- `email`: 邮箱（唯一）
- `phone`: 手机号（唯一）
- `create_time`: 创建时间
- `update_time`: 更新时间

### role 表
角色表，包含以下字段：
- `id`: 主键，自增
- `name`: 角色名称（唯一）
- `description`: 角色描述
- `create_time`: 创建时间
- `update_time`: 更新时间

### user_role 表
用户角色关联表，包含以下字段：
- `id`: 主键，自增
- `user_id`: 用户ID
- `role_id`: 角色ID
- `create_time`: 创建时间

## 配置说明

### MySQL 配置 (conf/my.cnf)
- 字符集：utf8mb4
- 时区：Asia/Shanghai (+8:00)
- 最大连接数：200
- InnoDB缓冲池：256M
- 查询缓存：32M
- 慢查询日志：启用（>2秒）

### 数据持久化
数据存储在 Docker 卷 `mysql_data` 中，容器删除后数据不会丢失。

## 故障排除

### 1. 端口冲突
如果 3306 端口被占用，修改 `docker-compose.yml` 中的端口映射：
```yaml
ports:
  - "3307:3306"  # 改为其他端口
```

### 2. 权限问题
确保脚本有执行权限：
```bash
chmod +x *.sh
```

### 3. 容器启动失败
查看详细日志：
```bash
docker-compose logs mysql
```

### 4. 连接被拒绝
等待容器完全启动（通常需要10-30秒），然后重试连接。

## 安全建议

1. **生产环境**：修改默认密码
2. **网络安全**：限制数据库访问IP
3. **备份策略**：定期备份数据库
4. **监控**：启用慢查询日志监控