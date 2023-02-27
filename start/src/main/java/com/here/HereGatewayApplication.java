package com.here;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway启动类
 * @author Lzk
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HereGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HereGatewayApplication.class, args);
    }
}