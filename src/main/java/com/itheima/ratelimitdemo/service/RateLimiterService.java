package com.itheima.ratelimitdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public interface RateLimiterService {



    /*
     * 试图获取一个令牌
     * @param key 限流标识
     * @return true: 获取令牌, false:未获取到
     *
     */

    boolean tryAcquire(String key);

}
