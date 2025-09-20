package com.msc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务启动类
 * 提供统一的API入口和负载均衡
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("=================================");
        System.out.println("  MultiShop Gateway 启动成功！");
        System.out.println("  端口: 8080");
        System.out.println("  管理端点: http://localhost:8080/actuator");
        System.out.println("=================================");
    }
}