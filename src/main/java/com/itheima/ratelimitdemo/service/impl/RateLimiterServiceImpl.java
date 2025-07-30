package com.itheima.ratelimitdemo.service.impl;

import com.itheima.ratelimitdemo.service.RateLimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    @Autowired
    RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> script;

    @Value("${ratelimit.capacity:10}")
    private long capacity;

    @Value("${ratelimit.refillTokens:1}")
    private long refillTokens;

    @Value("${ratelimit.refillIntervalMillis:1000}")
    private long refillInterval;

    @PostConstruct
    public void  loadScript()throws Exception{
        String lua = new String(
                new ClassPathResource("token_bucket.lua")
                        .getInputStream()
                        .readAllBytes(),
                StandardCharsets.UTF_8);

        script = new DefaultRedisScript<>();
        script.setScriptText(lua);

        script.setResultType(Long.class);
    }


    @Override
    public boolean tryAcquire(String key){
        // KEYS[1] = key
        // ARGV[1] = now, ARGV[2] = capacity, ARGV[3] = refillTokens, ARGV[4] = refillInterval

        log.info("执行redis");
        List<String> keys = Collections.singletonList(key);
        Object[] args = new Object[]{
                System.currentTimeMillis(),
                capacity,
                refillTokens,
                refillInterval
        };

        Long allowed =(Long) redisTemplate.execute(script, keys, args);

        return allowed == 1L;
    };

}
