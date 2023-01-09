package com.example.openfeigntest.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 定义一个openfeign的接口
 * 提供一个 @FeignClient的注解,生命这是一个feign的客户端
 * 注解的value就是eureka服务中心的实例组名称
 *
 */

@FeignClient(value = "PROVIDER9001",url = "http://localhost:9001")
public interface OpenFeignClient {
    /**
     * 接口中定义的每个方法都是远程调用
     * 地秤自动发送一个http请求
     * 请求地址是：http://PROVIDER9001
     */

    /**
     * 方法的名称可以是任意，只需要返回类型跟远程对应的controller请求的内容返回值类型一致
     * @return
     */

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String method1();

    @RequestMapping("/getwithparams")
    public String getWithParams(@RequestParam("name") String name, @RequestParam("age") int age);

    @RequestMapping("/restful/{name}/{id}")
    public String restful(@PathVariable("name") String name,@PathVariable("id") int id);
}
