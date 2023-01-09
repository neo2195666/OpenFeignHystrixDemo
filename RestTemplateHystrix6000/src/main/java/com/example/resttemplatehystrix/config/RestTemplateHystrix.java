package com.example.resttemplatehystrix.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateHystrix {

    @Bean
    @LoadBalanced
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }


}
