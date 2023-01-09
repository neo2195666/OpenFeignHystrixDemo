package com.example.resttemplatehystrix.controller;

import com.example.resttemplatehystrix.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/")
    public String get(){
        String result = testService.get();
        return result;
    }

}
