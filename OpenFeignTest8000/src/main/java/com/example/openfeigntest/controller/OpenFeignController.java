package com.example.openfeigntest.controller;

import com.example.openfeigntest.feign.OpenFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenFeignController {
    @Autowired
    private OpenFeignClient openFeignClient;

    @RequestMapping(value = "/hello",produces = {"text/html;charset=UTF-8"})
    public String getNoParam(){
        return openFeignClient.method1();
    }

    @RequestMapping(value = "/getwithparams",produces = {"text/html;charset=UTF-8"})
    public String getWithParams(String name,int age){
        return openFeignClient.getWithParams(name,age);
    }

    @RequestMapping(value = "/restful/{name}/{id}",produces = {"text/html;charset=UTF-8"})
    public String restful(@PathVariable("name") String name, @PathVariable("id") int id){
        return openFeignClient.restful(name,id);
    }
}
