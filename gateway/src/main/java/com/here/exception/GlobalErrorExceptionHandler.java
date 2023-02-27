package com.here.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.here.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Gateway Filter 全局异常处理
 *
 * @author Lzk
 */
@Slf4j
@Configuration
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        if (serverHttpResponse.isCommitted()) {
            return Mono.error(ex);
        }

        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Response response;
        if (ex instanceof BizException) {
            response = Response.buildFailure("BIZ_ERROR", ex.getMessage());
            log.error(ex.getMessage(), ex);
        } else if (ex instanceof IllegalStateException) {
            response = Response.buildFailure("PARAM_ERROR", ex.getMessage());
            log.error(ex.getMessage(), ex);
        } else if (ex instanceof UnknownHostException) {
            response = Response.buildFailure("NOT_FOUND", "转发服务不存在");
            log.error(ex.getMessage(), ex);
        } else if (ex instanceof ConnectException) {
            response = Response.buildFailure("BAD_GATEWAY", "服务连接失败");
            log.error(ex.getMessage(), ex);
        } else {
            response = Response.buildFailure("BAD_GATEWAY", ex.getMessage());
            log.error(ex.getMessage(), ex);
        }

        return serverHttpResponse.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = serverHttpResponse.bufferFactory();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(response));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return null;
            }
        }));
    }

}
