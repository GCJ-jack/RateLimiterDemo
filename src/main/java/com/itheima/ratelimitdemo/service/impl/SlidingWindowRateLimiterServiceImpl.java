package com.itheima.ratelimitdemo.service.impl;

import com.itheima.ratelimitdemo.service.WindowRateLimiterService;
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
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SlidingWindowRateLimiterServiceImpl implements WindowRateLimiterService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    private DefaultRedisScript<Long> script;

    /** 窗口大小，毫秒 */
    @Value("${ratelimit.windowMillis:60000}")
    private long windowMillis;

    /** 窗口内最大请求数 */
    @Value("${ratelimit.limit:100}")
    private long limit;

    @PostConstruct
    public void loadScript() throws Exception {
        String lua = new String(
                new ClassPathResource("sliding_window.lua").getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );
        script = new DefaultRedisScript<>();
        script.setScriptText(lua);
        script.setResultType(Long.class);
    }

    @Override
    public boolean tryAcquire(String key) {
        List<String> keys = Collections.singletonList(key);
        List<String> args = Arrays.asList(
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(windowMillis),
                String.valueOf(limit)
        );
        // 把 List<String> 平铺成 String[]
        String[] argv = args.toArray(new String[0]);
        Long allowed = redisTemplate.execute(script, keys, (Object[]) argv);
        return allowed != null && allowed == 1L;
    }


}
