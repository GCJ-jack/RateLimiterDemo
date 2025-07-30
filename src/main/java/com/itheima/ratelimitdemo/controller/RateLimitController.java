package com.itheima.ratelimitdemo.controller;


import com.itheima.ratelimitdemo.service.RateLimiterService;
import com.itheima.ratelimitdemo.service.WindowRateLimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class RateLimitController {

    @Autowired
    RateLimiterService rateLimiterService;

    @Autowired
    private WindowRateLimiterService windowRateLimiterService;

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestParam(defaultValue =  "default") String user){
        String bucketKey = "rl:" + user;

        boolean ok = rateLimiterService.tryAcquire(bucketKey);

        if(!ok){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests - 请稍后再试");

        }

        return ResponseEntity.ok("Hello, " + user + "! 资源访问成功");
    }


    /**
     * 滑动窗口限流示例：
     * GET /api/sliding-resource?user=alice
     */
    @GetMapping("/sliding-resource")
    public ResponseEntity<String> slidingResource(@RequestParam(defaultValue = "default") String user) {
        log.info("测试");
        String key = "sw:" + user;  // 每个 user 独立一个窗口
        boolean ok = windowRateLimiterService.tryAcquire(key);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("429 Too Many Requests — 请稍后再试");
        }
        return ResponseEntity.ok("Hello, " + user + "! 滑动窗口放行，资源访问成功");
    }

}
