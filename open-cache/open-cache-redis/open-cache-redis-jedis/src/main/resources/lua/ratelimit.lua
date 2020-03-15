local key = KEYS[1]
local limit = tonumber(ARGV[1])
local expire_time = ARGV[2]
local random = tonumber(ARGV[3])
local hashkey = ARGV[5]
local ip = ARGV[4];
local is_hash_exists = redis.call("EXISTS", hashkey)
if is_hash_exists == 1 then
     local times =  redis.call("HINCRBY", hashkey,ip,1)
     if times > 50 then
        return 0
     end
else
    redis.call("hset", hashkey, ip ,1)
    redis.call("EXPIRE", hashkey, expire_time)
end

local is_exists = redis.call("EXISTS", key)
if is_exists == 1 then
    local num = redis.call("INCR", key)
    if num > limit then
       if num > limit*100 then
            if random<20 then
                 return 1
            end
       elseif num > (limit*10) then
            if random<50 then
                 return 1
            end
       elseif num > (limit*3) then
           if random<80 then
                return 1
           end
       else
           return 1
       end
       return 0
    else
        return 1
    end
else
    redis.call("SET", key, 1)
    redis.call("EXPIRE", key, expire_time)
    return 1
end
