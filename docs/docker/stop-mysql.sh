#!/bin/bash

# MultiShop Core MySQL Docker 停止脚本

echo "=== MultiShop Core MySQL Docker 停止脚本 ==="

# 进入脚本所在目录
cd "$(dirname "$0")"

# 检查容器是否运行
if ! docker ps | grep -q "multishop-mysql"; then
    echo "MySQL 容器未运行"
    exit 0
fi

echo "停止 MySQL 容器..."
docker-compose down

echo "是否要删除数据卷? (这将删除所有数据库数据)"
read -p "删除数据卷? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "删除数据卷..."
    docker-compose down -v
    echo "✅ MySQL 容器已停止并删除数据卷"
else
    echo "✅ MySQL 容器已停止，数据卷保留"
fi