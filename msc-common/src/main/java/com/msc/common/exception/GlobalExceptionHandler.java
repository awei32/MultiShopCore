package com.msc.common.exception;

import com.alibaba.fastjson2.JSON;
import com.msc.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关全局异常处理器
 * 统一处理网关层面的异常
 */
@Component
@Order(-1)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpStatus status;
        String message;

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            status = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            message = responseStatusException.getReason();
        } else if (ex instanceof BusinessException) {
            // 处理业务异常
            BusinessException businessException = (BusinessException) ex;
            status = HttpStatus.BAD_REQUEST;
            message = businessException.getMessage();
            return writeErrorResponse(response, status, message, exchange.getRequest().getURI().getPath(), businessException.getCode());
        } else if (ex instanceof RuntimeException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal server error: " + ex.getMessage();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Unknown error occurred";
        }

        return writeErrorResponse(response, status, message, exchange.getRequest().getURI().getPath(), status.value());
    }
    
    /**
     * 写入错误响应
     * @param response 响应对象
     * @param status HTTP状态码
     * @param message 错误消息
     * @param path 请求路径
     * @param code 业务错误码
     * @return Mono<Void>
     */
    private Mono<Void> writeErrorResponse(ServerHttpResponse response, HttpStatus status, String message, String path, Integer code) {
        response.setStatusCode(status);

        Result<Void> result = Result.error(code, message);
        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}