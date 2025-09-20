package com.msc.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 负载均衡过滤器
 * 记录请求路由信息和负载均衡状态
 */
@Slf4j
@Component
public class LoadBalancerFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String remoteAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        
        log.info("负载均衡请求 - 路径: {}, 方法: {}, 客户端IP: {}", path, method, remoteAddress);
        
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put("startTime", startTime);
        
        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                log.info("请求完成 - 路径: {}, 耗时: {}ms", path, duration);
            })
        );
    }

    @Override
    public int getOrder() {
        return -1; // 优先级最高
    }
}