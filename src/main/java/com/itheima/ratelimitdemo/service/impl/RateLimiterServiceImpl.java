package com.itheima.ratelimitdemo.service.impl;

import com.itheima.ratelimitdemo.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public boolean tryAcquire(String key){
        return false;
    };
}
