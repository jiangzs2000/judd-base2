package spring.shuyuan.judd.base.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.google.common.base.Strings;

public class CacheServiceImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private RedisTemplate<String, Object> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;

    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void putObject(String key, Object value) {
        if (Strings.isNullOrEmpty(key) || null == value) return;
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    public void putObject(String key, Object value, int expire) {
        if (Strings.isNullOrEmpty(key) || null == value) return;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        this.expire(key, expire);
    }

    public void deleteObjectByKey(String key) {
        if (null == key) return;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.getOperations().delete(key);
    }

    public Object getObject(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    public void putString(String key, String value) {
        if (Strings.isNullOrEmpty(key) || null == value) return;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value);
    }

    public void putString(String key, String value, int expire) {
        this.putString(key, value);
        this.expire(key, expire);
    }

    public String getString(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key);
    }

    public void expire(String key, int expire) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    //redis Hash操作
    @Override
    public void putStringToHash(String key, String hashKey, String value) {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(hashKey) || null == value) return;
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public String getStringFromHash(String key, String field) {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(field)) return null;
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    @Override
    public Long deleteHashKey(String key, Object... fields) {
        if (Strings.isNullOrEmpty(key)) return -1L;
        return stringRedisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public boolean existHash(String key, String field) {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(field)) return false;
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Map getAllFromHash(String key) {
        if (Strings.isNullOrEmpty(key)) return null;
        return stringRedisTemplate.opsForHash().entries(key);
    }

}
