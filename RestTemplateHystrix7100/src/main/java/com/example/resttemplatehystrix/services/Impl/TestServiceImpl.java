package com.example.resttemplatehystrix.services.Impl;

import com.example.resttemplatehystrix.services.TestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

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
        return "(服务降级)服务器繁忙，稍等";
    }


    /**
     * 熔断：强化版降级
     * @return
     */
    @Override
    @HystrixCommand(fallbackMethod = "downgrade",
            commandProperties = {
                    //开启熔断服务
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ENABLED,value = "true"),
                    //计数
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD,value = "2"),
                    //错误的百分比
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE,value = "50"),
                    //熔断多长时间
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS,value = "3000"),
                    //强制打开熔断器
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_OPEN,value = "false"),
                    //强制关闭熔断器
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_CLOSED,value = "false"),
                    //熔断多长时间
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS,value = "10000"),

            })
    public String getCircuitBreaker() {
        String url = "http://EUREKACLIENT/getWithNoParams";
        System.out.println("==========开始使RestTemplate进行熔断测试=========");
        //url,返回值类型
        String result = restTemplate.getForObject(url,String.class);
        System.out.println(result);
        System.out.println("==========RestTemplate远熔断测试结束=========");
        return result;
    }

    public String downgrade(){
        return "(服务熔断)服务器繁忙，稍等";
    }


    /**
     * 请求缓存。
     * 1、查询redis是否有缓存，有的话直接返回缓存。
     * 2、没有的话，执行当前方法
     * 3、保存访问结果到redis
     * 4、返回结果给客户端
     *
     * cacheNames -  redis中key的前缀(字符串要写在''中，方法的参数用 #参数名 来表示)
     * key - redis中key的后缀
     * @param name
     * @param age
     * @return
     */
    @Override
    @Cacheable(cacheNames = "hystrix",key = "'params('+ #name +','+ #age +')'")
    public String getWithParams(String name, int age) {
        String url = "http://EUREKACLIENT/getWithParams?name={1}&age={2}";
        System.out.println("==========开始使RestTemplate进行缓存测试=========");
        String result = restTemplate.getForObject(url,String.class,name,age);
        System.out.println("==RestTemplate缓存结果==\n" + result);

        return result;
    }

    /**
     * 向redis中新增一个数据
     * @param name
     * @param age
     * @return
     */
    @Override
    @CachePut(cacheNames = "newhystrix",key = "'params('+ #name +','+ #age +')'")
    public String add(String name, int age) {
        String url = "http://EUREKACLIENT/getWithParams?name={1}&age={2}";
        System.out.println("==========新增redis缓存数据测试=========");
        String result = restTemplate.getForObject(url,String.class,name,age);
        System.out.println("==redis缓存数据结果==\n" + result);
        return result;
    }

    /**
     * 删除redis数据
     * @param name
     * @param age
     * @return
     * 删除缓存中的所有键值对
     * @CacheEvict(allEntries = true,cacheNames = "hystrix")
     */
    @Override
    //@CacheEvict(cacheNames = "hystrix",key = "'params('+ #name +','+ #age +')'")
    @CacheEvict(allEntries = true,cacheNames = "newhystrix")
    public String delete(String name, int age) {
        String url = "http://EUREKACLIENT/delete?name={1}&age={2}";
        System.out.println("==========删除redis缓存数据测试=========");
        String result = restTemplate.getForObject(url,String.class,name,age);
        System.out.println("==删除redis缓存数据结果==\n" + result);
        return result;
    }

    /**
     * 请求合并
     * 需要给定两个阀值
     * 1、等待多久
     * 2、合并多少请求
     * @param id
     * @return
     * @HystrixCollapser表示当前方法是一个要合并的请求处理方法
     * 要合并的请求处理方法不会执行。而且返回结果忽略
     * 注解的参数 batchMethod - 合并请求后，真正执行的批处理方法名称
     * 注解的参数 scope -  要合并的请求的范围。可选值。request|global
     *      request - 一次请求中，若干次远程访问的合并
     *      global - 若干请求做合并
     *
     * collapserProperties - 合并参数，就是用于配置合并阀值的
     *
     * 请求合并执行流程：
     * 1、根据阀值，等待收集若干请求。合并请求参数
     * 2、调用批处理方法batchMethod，把合并的请求参数传递给批处理方法
     * 3、批处理方法执行，远程服务返回后，把返回的集合拆分成若干个Future
     * 4、把拆分后的返回结果future，传递给上层的调用者(controller)
     *
     */
    @Override
    @HystrixCollapser(batchMethod = "batchMethod1",scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,collapserProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.MAX_REQUESTS_IN_BATCH,value = "2"),
            @HystrixProperty(name = HystrixPropertiesManager.TIMER_DELAY_IN_MILLISECONDS,value = "800")
    })
    public Future<String> details(Integer id) {
        System.out.println("执行合并 => 参数是：" + id);
        return null;
    }

    /**
     * 真正执行的批处理方法
     * 1、返回值类型是，远程服务返回的结果类型
     * 2、方法参数 是要合并的方法参数的集合
     * 3、方法名无要求
     * 4、使用@HystrixCommand注解来修饰方法
     * @return
     */
    @HystrixCommand
    public List<String> batchMethod1(List<Integer> params){
        String url = "http://EUREKACLIENT/batch";
        System.out.println("开始访问远程批处理 => " + url + "  ;参数是：" + params);
        //访问远程的批处理方法，请求提传参。
        List<String> result = restTemplate.postForObject(url,params,List.class);
        System.out.println("远程批处理结束 => " + result);
        return result;
    }

}
