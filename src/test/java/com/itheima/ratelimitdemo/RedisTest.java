package com.itheima.ratelimitdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = Main.class)// 指定主配置类
public class RedisTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    void testSpring(){
        redisTemplate.opsForValue().set("name","chaojun");
    }

}
