package com.example.resttemplatehystrix.services.Impl;

import com.example.resttemplatehystrix.services.TestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private RestTemplate restTemplate;
    @Override
    @HystrixCommand(fallbackMethod = "hystrixGet")
    public String get() {
        String url = "http://EUREKACLIENT/getWithNoParams";
        System.out.println("==========开始使RestTemplate进行远程访问=========");
        //url,返回值类型
        String result = restTemplate.getForObject(url,String.class);
        System.out.println(result);
        System.out.println("==========开始使RestTemplate远程访问结束=========");
        return result;
    }

    public String hystrixGet() {
        String url = "http://EUREKACLIENT/getWithNoParams";
        System.out.println("==========开始使hystrixGet进行远程访问=========");
        //url,返回值类型
        String result = restTemplate.getForObject(url,String.class);
        System.out.println(result);
        System.out.println("==========开始使hystrixGet远程访问结束=========");
        return result;
    }

}
