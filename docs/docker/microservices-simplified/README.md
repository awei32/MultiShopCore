# MultiShop Core 简化版微服务数据库配置

## 架构说明

使用 Spring Cloud Gateway 进行负载均衡时，可以简化数据库集群配置：

```
客户端请求 → Spring Cloud Gateway → 微服务实例集群 → 简化的MySQL集群
```

## 与复杂版本的区别

### 复杂版本（ProxySQL方案）
- 每个微服务：1主 + 2从 + ProxySQL
- 端口占用多，配置复杂
- 数据库层面负载均衡

### 简化版本（Spring Cloud Gateway方案）
- 每个微服务：1主 + 1从（或单实例）
- 配置简单，资源占用少
- 应用层面负载均衡

## 推荐配置方案

### 方案一：主从配置（推荐）
每个微服务使用一主一从：
- 主库：处理写操作
- 从库：处理读操作
- Spring Boot 配置读写分离

### 方案二：单实例配置（开发环境）
每个微服务使用单个MySQL实例：
- 适合开发和测试环境
- 配置最简单

## 为什么可以简化？

1. **负载均衡在应用层**
   - Spring Cloud Gateway 自动发现和负载均衡微服务实例
   - 不需要数据库层面的复杂负载均衡

2. **微服务实例扩展**
   - 通过启动多个微服务实例来处理高并发
   - 数据库压力分散到不同的实例

3. **更好的监控和管理**
   - 统一的网关监控
   - 更容易的服务治理

## 配置示例

### Spring Cloud Gateway 路由配置
```yaml
spring:
  cloud:
    gateway:
      routes:
        # 用户服务路由
        - id: user-service
          uri: lb://msc-user
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=2
            
        # 订单服务路由  
        - id: order-service
          uri: lb://msc-order
          predicates:
            - Path=/api/order/**
          filters:
            - StripPrefix=2
            
        # 商品服务路由
        - id: product-service
          uri: lb://msc-product
          predicates:
            - Path=/api/product/**
          filters:
            - StripPrefix=2
```

### 微服务数据源配置（主从分离）
```yaml
# 用户服务配置
spring:
  datasource:
    master:
      jdbc-url: jdbc:mysql://localhost:3306/user_service_db
      username: user_service
      password: userdb123
      driver-class-name: com.mysql.cj.jdbc.Driver
    slave:
      jdbc-url: jdbc:mysql://localhost:3307/user_service_db
      username: user_service
      password: userdb123
      driver-class-name: com.mysql.cj.jdbc.Driver
```

## 部署建议

### 开发环境
- 使用单实例数据库配置
- 简单快速，便于调试

### 测试环境  
- 使用主从配置
- 模拟生产环境

### 生产环境
- 使用主从配置 + 数据库连接池优化
- 配置监控和备份策略

## 迁移步骤

如果你想从复杂版本迁移到简化版本：

1. **确保 Spring Cloud Gateway 配置正确**
2. **配置服务发现（Nacos）**
3. **简化数据库集群配置**
4. **更新微服务数据源配置**
5. **测试负载均衡效果**

这样的架构更符合微服务的设计理念，也更容易维护和扩展！