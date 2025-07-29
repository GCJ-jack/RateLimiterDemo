package com.itheima.ratelimitdemo;

import com.itheima.ratelimitdemo.entity.CartItem;
import com.itheima.ratelimitdemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest(classes = Main.class)// 指定主配置类
public class RedisTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testSpring(){
        redisTemplate.opsForValue().set("name","chaojun");

        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    void testObject(){

        redisTemplate.opsForValue().set("User",new User("chaojun",23));



        stringRedisTemplate.opsForValue().increment("counter", 1);
        System.out.println(stringRedisTemplate.opsForValue().get("counter"));

        redisTemplate.delete("User");

//        System.out.println(redisTemplate.opsForValue().get("User").toString());

    }

    @Test
    void demoHashOps(){

        redisTemplate.opsForHash().put("cart:2002","item:3003", new CartItem(3003,2));

        List<Object> items = redisTemplate.opsForHash().multiGet("cart:2002", Arrays.asList("item:3003","item:3004"));

        items.forEach(o -> System.out.println(
                o != null ? ((CartItem)o).getProductId() : "null"
        ));
        redisTemplate.opsForHash().delete("cart:2002", "item:3003");
    }

    @Test
    public void demoListOps(){
        redisTemplate.opsForList().leftPush("msgs","hello");
        redisTemplate.opsForList().leftPush("msgs","world");

        String last = (String) redisTemplate.opsForList().rightPop("msgs");

        System.out.println("popped:" + last);

        List<Object> range = redisTemplate.opsForList().range("msgs",0,-1);

        range.forEach(r -> System.out.println(r));
    }

    @Test
    public void demoSetOps(){
        redisTemplate.opsForSet().add("tags","java","spring","redis");

        Set<Object> tags = redisTemplate.opsForSet().members("tags");

        tags.forEach(t -> System.out.println(t));
    }

}
