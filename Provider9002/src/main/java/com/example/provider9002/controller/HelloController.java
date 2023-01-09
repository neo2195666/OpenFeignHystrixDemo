package com.example.provider9002.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "provider-9001";
    }

    @RequestMapping("/getwithparams")
    public String getWithParams(String name, int age){
        return "{name: + " + name + "; age: + " + age + "}";
    }

    @RequestMapping("/restful/{name}/{id}")
    public String restful(@PathVariable("name") String name, @PathVariable("id") int id){
        return "{name: + " + name + "; id: + " + id + "}";
    }
}
