package com.example.eureka_client.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    /**
     * 批量查询，主键在参数集合范围中
     * @param ids
     * @return
     */
    @RequestMapping("/batch")
    public List<String> batch(@RequestBody List<Integer> ids){
        List<String> result = new ArrayList<>();
        for(int i = 0; i < ids.size();i++)
            result.add("结果数据：" + i);
        return result;
    }

    @RequestMapping("/getWithNoParams")
    public String initController(){
        return "eureka client 8001 /getWithNoParams";
    }

    @RequestMapping("/getWithParams")
    public String getWithParams(String name,int age){
        return "{name:"+name+" ;age:"+age+"}";
    }

    @RequestMapping("/add")
    public String add(String name,int age){
        return  "{name:"+name+" ;age:"+age+"}";
    }

    @RequestMapping("/delete")
    public String delete(String name,int age){
        return  "删除数据{name:"+name+" ;age:"+age+"}";
    }
}
