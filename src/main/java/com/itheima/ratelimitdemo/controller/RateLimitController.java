package com.itheima.ratelimitdemo.controller;


import com.itheima.ratelimitdemo.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/api/test")
    public ResponseEntity<String> test(){
         boolean ok = rateLimiterService.tryAcquire("api_test");
    }

}
