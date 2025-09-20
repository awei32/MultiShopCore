package com.msc.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 网关配置类
 * 配置负载均衡和限流策略
 */
@Configuration
public class GatewayConfig {

    /**
     * IP限流器
     * 根据客户端IP进行限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress()
        );
    }

    /**
     * 用户限流器
     * 根据用户ID进行限流（需要从请求头或参数中获取）
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest()
                .getHeaders()
                .getFirst("user-id") != null ? 
                exchange.getRequest().getHeaders().getFirst("user-id") : "anonymous"
        );
    }

    /**
     * API路径限流器
     * 根据请求路径进行限流
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getPath().value()
        );
    }
}