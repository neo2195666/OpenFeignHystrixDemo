package com.example.openfeigntest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.example.openfeigntest.feign"})
public class OpenFeignTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenFeignTestApplication.class, args);
    }

}
