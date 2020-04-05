package io.project.app.cache.resources;

import io.project.app.common.api.CacheService;
import io.project.app.common.services.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@RefreshScope
@RestController
public class CacheProviderResource implements CacheService {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            String strValue = jedis.get(key);

            T objValue = JsonUtil.stringToBean(strValue, clazz);
            return objValue;
        } finally {

            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String strValue = JsonUtil.beanToString(value);
            if (strValue == null || strValue.length() <= 0) {
                return false;
            }
            jedis.set(key, strValue);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(String key, T value, int expireSeconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String strValue = JsonUtil.beanToString(value);
            if (strValue == null || strValue.length() <= 0) {
                return false;
            }
            //jedis.set(key, strValue, nxxx, expx, expireSeconds);
            SetParams setParams = new SetParams();
            setParams.ex(expireSeconds);

            jedis.set(key, strValue, setParams);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(key);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long del = jedis.del(key);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    public void likeDynamic(int dynamicId, int userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "like:dynamic:" + dynamicId;
            if (jedis.sismember(key, "" + userId)) {
                jedis.srem(key, "" + userId);
            } else {
                jedis.sadd(key, "" + userId);
            }
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean limitFrequency(int userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long nowTs = System.currentTimeMillis();
            int period = 60, maxCount = 5;
            String key = "frequency:limit:" + userId;
            jedis.zadd(key, nowTs, "" + nowTs);
            jedis.zremrangeByScore(key, 0, nowTs - period * 1000);
            return jedis.zcard(key) > maxCount;
        } finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
