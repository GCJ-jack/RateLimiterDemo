-- KEYS[1] = 令牌桶 key
-- ARGV[1] = 当前时间戳（毫秒）
-- ARGV[2] = 桶容量(capacity)
-- ARGV[3] = 每次填充的令牌数量(refillTokens)
-- ARGV[4] = 填充间隔(refillIntervalMillis)

local key = KEYS[1]
local now = tonumber(ARGV[1])
local capacity    = tonumber(ARGV[2])
local refillTokens = tonumber(ARGV[3])
local refillInterval = tonumber(ARGV[4])


local bucket = redis.call("HMGET", key, "tokens", "last_refill_ts")
local tokens = tonumber(bucket[1])
local last_ts = tonumber(bucket[2])


if tokens == nil then
  tokens = capacity
  last_ts = now
end


-- 计算经过时间，添充令牌
local delta = math.floor((now - last_ts) / refillInterval) * refillTokens
if delta > 0 then
  tokens = math.min(capacity, tokens + delta)
  last_ts = now
end


local allowed = 0
if tokens > 0 then
  tokens = tokens - 1
  allowed = 1
end


-- 更新回 Redis
redis.call("HMSET", key, "tokens", tokens, "last_refill_ts", last_ts)
-- 设置过期，避免无限增长
redis.call("PEXPIRE", key, refillInterval * capacity)

return allowed
