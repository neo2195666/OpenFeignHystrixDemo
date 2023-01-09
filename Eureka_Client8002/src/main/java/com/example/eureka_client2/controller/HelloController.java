package com.example.eureka_client2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/getWithNoParams")
    public String initController(){
        return "eureka client 8002 /getWithNoParams";
    }
}
