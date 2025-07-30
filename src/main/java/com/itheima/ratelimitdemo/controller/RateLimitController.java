package com.itheima.ratelimitdemo.controller;


import com.itheima.ratelimitdemo.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestParam(defaultValue =  "default") String user){
        String bucketKey = "rl:" + user;

        boolean ok = rateLimiterService.tryAcquire(bucketKey);

        if(!ok){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests - 请稍后再试");

        }

        return ResponseEntity.ok("Hello, " + user + "! 资源访问成功");
    }


}
