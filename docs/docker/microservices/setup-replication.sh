#!/bin/bash

# MySQL 主从复制配置脚本

if [ $# -eq 0 ]; then
    echo "用法: $0 <service-name>"
    echo "示例: $0 user-service"
    exit 1
fi

SERVICE_NAME=$1

echo "=== 配置 $SERVICE_NAME 主从复制 ==="

# 进入服务目录
cd "$(dirname "$0")/$SERVICE_NAME"

if [ ! -f "docker-compose.yml" ]; then
    echo "❌ 错误: $SERVICE_NAME/docker-compose.yml 文件不存在"
    exit 1
fi

# 等待主库启动完成
echo "⏳ 等待主库启动完成..."
sleep 30

# 获取主库状态
echo "📊 获取主库状态..."
MASTER_STATUS=$(docker exec ${SERVICE_NAME/-/_}_mysql_master_1 mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e "SHOW MASTER STATUS\G" 2>/dev/null)

if [ $? -ne 0 ]; then
    echo "❌ 无法连接到主库，请检查容器是否正常运行"
    exit 1
fi

# 提取binlog文件名和位置
BINLOG_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
BINLOG_POS=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')

echo "📝 主库状态:"
echo "  Binlog文件: $BINLOG_FILE"
echo "  Binlog位置: $BINLOG_POS"

if [ -z "$BINLOG_FILE" ] || [ -z "$BINLOG_POS" ]; then
    echo "❌ 无法获取主库状态信息"
    exit 1
fi

# 配置从库1
echo "⚙️  配置从库1..."
SLAVE1_CONTAINER="${SERVICE_NAME/-/_}_mysql_slave1_1"
docker exec $SLAVE1_CONTAINER mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e "
STOP SLAVE;
RESET SLAVE;
CHANGE MASTER TO 
    MASTER_HOST='${SERVICE_NAME/-/_}_mysql_master_1',
    MASTER_USER='replication',
    MASTER_PASSWORD='replication123',
    MASTER_LOG_FILE='$BINLOG_FILE',
    MASTER_LOG_POS=$BINLOG_POS;
START SLAVE;
" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ 从库1配置成功"
else
    echo "❌ 从库1配置失败"
fi

# 配置从库2
echo "⚙️  配置从库2..."
SLAVE2_CONTAINER="${SERVICE_NAME/-/_}_mysql_slave2_1"
docker exec $SLAVE2_CONTAINER mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e "
STOP SLAVE;
RESET SLAVE;
CHANGE MASTER TO 
    MASTER_HOST='${SERVICE_NAME/-/_}_mysql_master_1',
    MASTER_USER='replication',
    MASTER_PASSWORD='replication123',
    MASTER_LOG_FILE='$BINLOG_FILE',
    MASTER_LOG_POS=$BINLOG_POS;
START SLAVE;
" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ 从库2配置成功"
else
    echo "❌ 从库2配置失败"
fi

# 检查复制状态
echo "📊 检查复制状态..."
echo ""
echo "=== 从库1状态 ==="
docker exec $SLAVE1_CONTAINER mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e "SHOW SLAVE STATUS\G" 2>/dev/null | grep -E "(Slave_IO_Running|Slave_SQL_Running|Last_Error)"

echo ""
echo "=== 从库2状态 ==="
docker exec $SLAVE2_CONTAINER mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e "SHOW SLAVE STATUS\G" 2>/dev/null | grep -E "(Slave_IO_Running|Slave_SQL_Running|Last_Error)"

echo ""
echo "✅ $SERVICE_NAME 主从复制配置完成"
echo ""
echo "💡 提示:"
echo "  - 如果看到 Slave_IO_Running: Yes 和 Slave_SQL_Running: Yes，说明复制正常"
echo "  - 如果有错误，请检查 Last_Error 字段的信息"
echo "  - 可以通过以下命令手动检查状态:"
echo "    docker exec $SLAVE1_CONTAINER mysql -uroot -p${SERVICE_NAME//-/}db_root123 -e \"SHOW SLAVE STATUS\\G\""