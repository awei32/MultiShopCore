package com.msc.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 网关配置类
 * 配置路由规则和跨域处理
 */
@Configuration
public class GatewayConfig {

    /**
     * 配置路由规则
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 用户服务路由
                .route("msc-user", r -> r
                        .path("/api/user/**")
                        .uri("lb://msc-user"))
                
                // 商品服务路由（预留）
                .route("msc-product", r -> r
                        .path("/api/product/**")
                        .uri("lb://msc-product"))
                
                // 订单服务路由（预留）
                .route("msc-order", r -> r
                        .path("/api/order/**")
                        .uri("lb://msc-order"))
                
                // 支付服务路由（预留）
                .route("msc-payment", r -> r
                        .path("/api/payment/**")
                        .uri("lb://msc-payment"))
                
                .build();
    }

    /**
     * 配置跨域处理
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的源
        config.addAllowedOriginPattern("*");
        
        // 允许的请求头
        config.addAllowedHeader("*");
        
        // 允许的请求方法
        config.addAllowedMethod("*");
        
        // 允许携带认证信息
        config.setAllowCredentials(true);
        
        // 预检请求的缓存时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}