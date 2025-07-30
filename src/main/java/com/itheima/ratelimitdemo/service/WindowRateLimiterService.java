package com.itheima.ratelimitdemo.service;

public interface WindowRateLimiterService {

    /**
     * 尝试获取一个请求许可
     * @param key      限流 key
     * @return         true=放行，false=限流
     */
    boolean tryAcquire(String key);
}
