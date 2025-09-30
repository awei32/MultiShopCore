package com.msc.filter;

import com.alibaba.fastjson2.JSON;
import com.msc.common.constant.JwtConstants;
import com.msc.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT认证全局过滤器
 * 在网关层面进行JWT token验证
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // 使用公共常量类

    /**
     * 不需要认证的路径
     */
    private static final List<String> SKIP_AUTH_PATHS = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/captcha",
            "/actuator",
            "/favicon.ico"
    );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否需要跳过认证
        if (shouldSkipAuth(path)) {
            return chain.filter(exchange);
        }

        // 获取请求头中的token
        String authHeader = request.getHeaders().getFirst(JwtConstants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return handleUnauthorized(exchange, "Missing or invalid Authorization header");
        }

        // 提取token
        String token = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        
        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                return handleUnauthorized(exchange, "Invalid or expired token");
            }

            // 从token中获取用户信息
            Long userIdLong = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            // 将用户信息添加到请求头中，传递给下游服务
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(JwtConstants.USER_ID_HEADER, String.valueOf(userIdLong))
                    .header(JwtConstants.USERNAME_HEADER, username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            return handleUnauthorized(exchange, "Token validation failed: " + e.getMessage());
        }
    }

    /**
     * 检查路径是否需要跳过认证
     */
    private boolean shouldSkipAuth(String path) {
        return SKIP_AUTH_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * 处理未授权请求
     */
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.UNAUTHORIZED.value());
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());

        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 设置过滤器优先级，数值越小优先级越高
        return -100;
    }
}