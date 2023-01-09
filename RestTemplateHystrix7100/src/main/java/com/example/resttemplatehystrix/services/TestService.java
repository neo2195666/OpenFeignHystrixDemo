package com.example.resttemplatehystrix.services;

import java.util.concurrent.Future;

public interface TestService {
    public String get();

    public String getCircuitBreaker();

    String getWithParams(String name, int age);

    String add(String name,int age);

    String delete(String name,int age);

    Future<String> details(Integer id);
}
