-- KEYS[1]      限流 key（比如 "rl:user:alice"）
-- ARGV[1]      当前时间戳（毫秒）
-- ARGV[2]      时间窗口大小 windowMillis（毫秒）
-- ARGV[3]      最大请求数 limit

local key = KEYS[1]
local now = tonumber(ARGV[1])
local windowMillis = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])

-- 清理窗口之外的旧数据
redis.call("ZREMRANGEBYSCORE", key, "-inf", now - windowMillis)

-- 统计当前窗口内的请求数
local cnt = redis.call("ZCARD", key)

if cnt < limit then
    -- 可以放行：记录本次请求的时间戳为 member，score 也用时间戳
    redis.call("ZADD", key, now, now)
    -- 设置过期，避免长时间没请求时 key 占用
    redis.call("PEXPIRE", key, windowMillis)
    return 1
else
    -- 拒绝
    return 0
end
