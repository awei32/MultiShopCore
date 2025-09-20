#!/bin/bash

# MultiShop Core 微服务数据库集群统一启动脚本

echo "=== MultiShop Core 微服务数据库集群启动脚本 ==="

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo "❌ 错误: Docker 未运行，请先启动 Docker"
    exit 1
fi

# 进入脚本所在目录
cd "$(dirname "$0")"

# 定义服务列表
SERVICES=("user-service" "order-service" "product-service")
SERVICE_PORTS=("用户服务:3306-3308,6033,6032" "订单服务:3316-3318,6043,6042" "商品服务:3326-3328,6053,6052")

# 显示菜单
echo ""
echo "请选择要启动的服务:"
echo "1) 启动所有微服务数据库集群"
echo "2) 启动用户服务数据库集群"
echo "3) 启动订单服务数据库集群"
echo "4) 启动商品服务数据库集群"
echo "5) 停止所有微服务数据库集群"
echo "6) 查看所有服务状态"
echo "7) 配置主从复制"
echo "0) 退出"
echo ""

read -p "请输入选项 (0-7): " choice

case $choice in
    1)
        echo "🚀 启动所有微服务数据库集群..."
        for service in "${SERVICES[@]}"; do
            echo "启动 $service 数据库集群..."
            cd "$service"
            if [ -f "docker-compose.yml" ]; then
                docker-compose up -d
                if [ $? -eq 0 ]; then
                    echo "✅ $service 数据库集群启动成功"
                else
                    echo "❌ $service 数据库集群启动失败"
                fi
            else
                echo "❌ $service/docker-compose.yml 文件不存在"
            fi
            cd ..
            echo ""
        done
        ;;
    2)
        echo "🚀 启动用户服务数据库集群..."
        cd user-service
        docker-compose up -d
        cd ..
        ;;
    3)
        echo "🚀 启动订单服务数据库集群..."
        cd order-service
        docker-compose up -d
        cd ..
        ;;
    4)
        echo "🚀 启动商品服务数据库集群..."
        cd product-service
        docker-compose up -d
        cd ..
        ;;
    5)
        echo "🛑 停止所有微服务数据库集群..."
        for service in "${SERVICES[@]}"; do
            echo "停止 $service 数据库集群..."
            cd "$service"
            if [ -f "docker-compose.yml" ]; then
                docker-compose down
                echo "✅ $service 数据库集群已停止"
            fi
            cd ..
        done
        ;;
    6)
        echo "📊 查看所有服务状态..."
        echo ""
        echo "=== 容器状态 ==="
        docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(user-mysql|order-mysql|product-mysql|proxysql)"
        echo ""
        echo "=== 端口占用情况 ==="
        for i in "${!SERVICES[@]}"; do
            echo "${SERVICES[$i]}: ${SERVICE_PORTS[$i]}"
        done
        ;;
    7)
        echo "⚙️  配置主从复制..."
        echo "请选择要配置的服务:"
        echo "1) 用户服务"
        echo "2) 订单服务"
        echo "3) 商品服务"
        echo "4) 所有服务"
        read -p "请输入选项 (1-4): " repl_choice
        
        case $repl_choice in
            1) ./setup-replication.sh user-service ;;
            2) ./setup-replication.sh order-service ;;
            3) ./setup-replication.sh product-service ;;
            4) 
                for service in "${SERVICES[@]}"; do
                    ./setup-replication.sh "$service"
                done
                ;;
            *) echo "❌ 无效选项" ;;
        esac
        ;;
    0)
        echo "👋 退出脚本"
        exit 0
        ;;
    *)
        echo "❌ 无效选项，请重新选择"
        ;;
esac

echo ""
echo "=== 连接信息 ==="
echo "用户服务数据库:"
echo "  - 主库: localhost:3306"
echo "  - 从库1: localhost:3307"
echo "  - 从库2: localhost:3308"
echo "  - ProxySQL: localhost:6033"
echo "  - 数据库: user_service_db"
echo "  - 用户名: user_service / 密码: userdb123"
echo ""
echo "订单服务数据库:"
echo "  - 主库: localhost:3316"
echo "  - 从库1: localhost:3317"
echo "  - 从库2: localhost:3318"
echo "  - ProxySQL: localhost:6043"
echo "  - 数据库: order_service_db"
echo "  - 用户名: order_service / 密码: orderdb123"
echo ""
echo "商品服务数据库:"
echo "  - 主库: localhost:3326"
echo "  - 从库1: localhost:3327"
echo "  - 从库2: localhost:3328"
echo "  - ProxySQL: localhost:6053"
echo "  - 数据库: product_service_db"
echo "  - 用户名: product_service / 密码: productdb123"
echo ""
echo "=== 常用命令 ==="
echo "查看日志: docker-compose logs -f [服务名]"
echo "进入容器: docker exec -it [容器名] bash"
echo "连接数据库: docker exec -it [容器名] mysql -u[用户名] -p[密码] [数据库名]"
echo "停止服务: docker-compose down"
echo "完全清理: docker-compose down -v"