package com.example.openfeigntest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String hello(){
        return "OpenFeign-9002";
    }
}
