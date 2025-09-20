#!/bin/bash

# MultiShop Core MySQL Docker 启动脚本

echo "=== MultiShop Core MySQL Docker 启动脚本 ==="

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo "错误: Docker 未运行，请先启动 Docker"
    exit 1
fi

# 进入脚本所在目录
cd "$(dirname "$0")"

# 检查必要文件是否存在
if [ ! -f "docker-compose.yml" ]; then
    echo "错误: docker-compose.yml 文件不存在"
    exit 1
fi

if [ ! -f "conf/my.cnf" ]; then
    echo "错误: conf/my.cnf 文件不存在"
    exit 1
fi

if [ ! -f "init/01-init-database.sql" ]; then
    echo "错误: init/01-init-database.sql 文件不存在"
    exit 1
fi

echo "检查现有容器..."
if docker ps -a | grep -q "multishop-mysql"; then
    echo "发现现有的 multishop-mysql 容器"
    read -p "是否要删除现有容器并重新创建? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "停止并删除现有容器..."
        docker-compose down -v
        echo "删除相关镜像..."
        docker rmi multishop-mysql 2>/dev/null || true
    else
        echo "启动现有容器..."
        docker-compose up -d
        exit 0
    fi
fi

echo "创建并启动 MySQL 容器..."
docker-compose up -d

# 等待MySQL启动
echo "等待 MySQL 启动..."
sleep 10

# 检查容器状态
if docker ps | grep -q "multishop-mysql"; then
    echo "✅ MySQL 容器启动成功!"
    echo ""
    echo "=== 连接信息 ==="
    echo "主机: localhost"
    echo "端口: 3306"
    echo "数据库: multishop_core"
    echo "用户名: multishop"
    echo "密码: multishop123"
    echo "Root密码: root123456"
    echo ""
    echo "=== 测试用户 ==="
    echo "管理员: admin / 123456"
    echo "普通用户: test / 123456"
    echo ""
    echo "=== 常用命令 ==="
    echo "查看日志: docker-compose logs -f mysql"
    echo "进入容器: docker exec -it multishop-mysql bash"
    echo "连接数据库: docker exec -it multishop-mysql mysql -u multishop -p multishop_core"
    echo "停止服务: docker-compose down"
    echo "停止并删除数据: docker-compose down -v"
else
    echo "❌ MySQL 容器启动失败!"
    echo "查看日志:"
    docker-compose logs mysql
    exit 1
fi