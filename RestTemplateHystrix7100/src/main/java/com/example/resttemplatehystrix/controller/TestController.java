package com.example.resttemplatehystrix.controller;

import com.example.resttemplatehystrix.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("/get")
    public String get(){
        String result = testService.getCircuitBreaker();
        return result;
    }

    @RequestMapping("/get1")
    public String get1(String name,int age){
        return testService.getWithParams(name,age);
    }

    @RequestMapping("/add")
    public String add(String name,int age){
        return testService.add(name,age);
    }

    @RequestMapping("/delete")
    public String delete(String name,int age){
        return testService.delete(name,age);
    }

    /**
     * Future 是java并发包中的一个接口
     * 代表线程异步处理，线程执行最终结果
     * 不调用get方法，线程是一部执行的。当调用get方法后，线程开始阻塞等待。等待异步执行结果，并返回结果。
     * @param id
     * @return
     */
    @RequestMapping("/details")
    public String details(Integer id){
        Future<String> result = testService.details(id);
        try{
            return result.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
