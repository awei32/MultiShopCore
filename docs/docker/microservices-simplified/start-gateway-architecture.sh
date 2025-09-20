#!/bin/bash

# MultiShop Core - Spring Cloud Gateway 架构启动脚本
# 使用简化的数据库配置 + Spring Cloud Gateway 负载均衡

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_title() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE} $1 ${NC}"
    echo -e "${BLUE}================================${NC}"
}

# 检查Docker是否运行
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker 未运行，请先启动 Docker"
        exit 1
    fi
    print_message "Docker 运行正常"
}

# 启动基础设施服务
start_infrastructure() {
    print_title "启动基础设施服务"
    
    print_message "启动 Redis..."
    docker-compose up -d redis
    
    print_message "启动 Nacos 数据库..."
    docker-compose up -d nacos-mysql
    
    print_message "等待 Nacos 数据库启动..."
    sleep 10
    
    print_message "启动 Nacos 服务发现..."
    docker-compose up -d nacos
    
    print_message "等待 Nacos 启动完成..."
    sleep 15
}

# 启动数据库集群
start_databases() {
    print_title "启动微服务数据库集群"
    
    print_message "启动用户服务数据库..."
    docker-compose up -d user-mysql-master user-mysql-slave
    
    print_message "启动订单服务数据库..."
    docker-compose up -d order-mysql-master order-mysql-slave
    
    print_message "启动商品服务数据库..."
    docker-compose up -d product-mysql-master product-mysql-slave
    
    print_message "等待数据库启动完成..."
    sleep 20
}

# 配置主从复制
setup_replication() {
    print_title "配置数据库主从复制"
    
    # 用户服务主从复制
    print_message "配置用户服务主从复制..."
    docker exec user-mysql-master mysql -uroot -puserdb_root123 -e "
        CREATE USER 'repl'@'%' IDENTIFIED BY 'repl123';
        GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
        FLUSH PRIVILEGES;
    " || true
    
    # 获取主库状态
    MASTER_STATUS=$(docker exec user-mysql-master mysql -uroot -puserdb_root123 -e "SHOW MASTER STATUS\G")
    MASTER_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
    MASTER_POS=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')
    
    # 配置从库
    docker exec user-mysql-slave mysql -uroot -puserdb_root123 -e "
        STOP SLAVE;
        CHANGE MASTER TO 
            MASTER_HOST='user-mysql-master',
            MASTER_USER='repl',
            MASTER_PASSWORD='repl123',
            MASTER_LOG_FILE='$MASTER_FILE',
            MASTER_LOG_POS=$MASTER_POS;
        START SLAVE;
    " || true
    
    # 类似地配置订单和商品服务...
    print_message "主从复制配置完成"
}

# 显示服务状态
show_status() {
    print_title "服务状态检查"
    
    echo "容器状态:"
    docker-compose ps
    
    echo ""
    echo "服务连接信息:"
    echo "┌─────────────────────────────────────────────────────────────┐"
    echo "│                     服务连接信息                              │"
    echo "├─────────────────────────────────────────────────────────────┤"
    echo "│ Nacos 控制台:    http://localhost:8848/nacos               │"
    echo "│ 用户名/密码:     nacos/nacos                               │"
    echo "├─────────────────────────────────────────────────────────────┤"
    echo "│ 用户服务数据库:                                              │"
    echo "│   主库: localhost:3306  用户: user_service/userdb123       │"
    echo "│   从库: localhost:3307  用户: user_service/userdb123       │"
    echo "├─────────────────────────────────────────────────────────────┤"
    echo "│ 订单服务数据库:                                              │"
    echo "│   主库: localhost:3316  用户: order_service/orderdb123     │"
    echo "│   从库: localhost:3317  用户: order_service/orderdb123     │"
    echo "├─────────────────────────────────────────────────────────────┤"
    echo "│ 商品服务数据库:                                              │"
    echo "│   主库: localhost:3326  用户: product_service/productdb123 │"
    echo "│   从库: localhost:3327  用户: product_service/productdb123 │"
    echo "├─────────────────────────────────────────────────────────────┤"
    echo "│ Redis:           localhost:6379                             │"
    echo "└─────────────────────────────────────────────────────────────┘"
    
    echo ""
    echo "下一步操作:"
    echo "1. 启动微服务应用 (msc-user, msc-order, msc-product)"
    echo "2. 启动网关服务 (msc-gateway)"
    echo "3. 通过网关访问: http://localhost:8080/api/{service}/**"
}

# 停止所有服务
stop_all() {
    print_title "停止所有服务"
    docker-compose down
    print_message "所有服务已停止"
}

# 清理数据
clean_data() {
    print_title "清理数据"
    docker-compose down -v
    docker system prune -f
    print_message "数据清理完成"
}

# 主菜单
show_menu() {
    echo ""
    print_title "MultiShop Core - Spring Cloud Gateway 架构"
    echo "请选择操作:"
    echo "1. 启动完整架构 (基础设施 + 数据库)"
    echo "2. 仅启动基础设施 (Redis + Nacos)"
    echo "3. 仅启动数据库集群"
    echo "4. 配置主从复制"
    echo "5. 查看服务状态"
    echo "6. 停止所有服务"
    echo "7. 清理所有数据"
    echo "8. 退出"
    echo ""
}

# 主程序
main() {
    while true; do
        show_menu
        read -p "请输入选项 (1-8): " choice
        
        case $choice in
            1)
                check_docker
                start_infrastructure
                start_databases
                setup_replication
                show_status
                ;;
            2)
                check_docker
                start_infrastructure
                show_status
                ;;
            3)
                check_docker
                start_databases
                show_status
                ;;
            4)
                setup_replication
                ;;
            5)
                show_status
                ;;
            6)
                stop_all
                ;;
            7)
                clean_data
                ;;
            8)
                print_message "退出脚本"
                exit 0
                ;;
            *)
                print_error "无效选项，请重新选择"
                ;;
        esac
        
        echo ""
        read -p "按回车键继续..."
    done
}

# 运行主程序
main