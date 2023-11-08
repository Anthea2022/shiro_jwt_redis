package camellia.prebookingshiro.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author anthea
 * @date 2023/11/7 15:24
 */
@Component
public class RedisUtil {
    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void set(String key, Object value, Long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.HOURS);
    }

    public static Object get(String key) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            return null;
        }
        if (redisTemplate.getExpire(key) <= 0) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }
}
