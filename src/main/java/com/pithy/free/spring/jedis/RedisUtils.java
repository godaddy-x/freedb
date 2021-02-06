package com.pithy.free.spring.jedis;

import com.pithy.free.spring.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.List;

public class RedisUtils {

    private JedisPool jedisPool;

    public RedisUtils(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 设置字符串缓存.
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间(秒)，0为不超时
     * @return
     */
    public void setString(String key, String value, int cacheSeconds) {
        Jedis jedis = null;
        try {
            assertNull(key);
            jedis = getResource();
            jedis.set(key, value);
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
    }

    /**
     * 获取字符串缓存.
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            assertNull(key);
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
        return value;
    }

    public long getLong(String key) {
        String value = getString(key);
        if (value == null) {
            return 0l;
        }
        return Long.parseLong(value);
    }

    public void setLong(String key, Long value, int cacheSeconds) {
        String s = value == null ? "0" : String.valueOf(value);
        setString(key, s, cacheSeconds);
    }

    public int getInt(String key) {
        String value = getString(key);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public void setInt(String key, Integer value, int cacheSeconds) {
        String s = value == null ? "0" : String.valueOf(value);
        setString(key, s, cacheSeconds);
    }

    public float getFloat(String key) {
        String value = getString(key);
        if (value == null) {
            return 0;
        }
        return Float.parseFloat(value);
    }

    public void setFloat(String key, Float value, int cacheSeconds) {
        String s = value == null ? "0" : String.valueOf(value);
        setString(key, s, cacheSeconds);
    }

    public double getDouble(String key) {
        String value = getString(key);
        if (value == null) {
            return 0;
        }
        return Double.parseDouble(value);
    }

    public void setDouble(String key, Double value, int cacheSeconds) {
        String s = value == null ? "0" : String.valueOf(value);
        setString(key, s, cacheSeconds);
    }

    public boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public void setBoolean(String key, Boolean value, int cacheSeconds) {
        String s = value == null ? "false" : String.valueOf(value);
        setString(key, s, cacheSeconds);
    }

    /**
     * 获取单个对象缓存.
     *
     * @param key 键
     * @return 值
     */
    public <T> T getObject(String key, Class<T> clz) {
        String value = getString(key);
        if (isNull(value)) {
            return null;
        }
        return JsonUtil.parseObject(value, clz);
    }

    /**
     * 获取列表对象缓存.
     *
     * @param key 键
     * @return 值
     */
    public <T> List<T> getObjectList(String key, Class<T> clz) {
        String value = getString(key);
        if (isNull(value)) {
            return null;
        }
        return JsonUtil.parseArray(value, clz);
    }

    /**
     * 设置缓存.
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public void setObject(String key, Object value, int cacheSeconds) {
        Jedis jedis = null;
        try {
            assertNull(key);
            if (value == null) {
                return;
            }
            jedis = getResource();
            jedis.set(key, JsonUtil.toJSONString(value));
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
    }

    /**
     * 获取rpush List缓存.
     *
     * @param key 键
     * @return 值
     */
    public <T> List<T> getRpushList(String key, Class<T> clz) {
        Jedis jedis = null;
        try {
            assertNull(key);
            jedis = getResource();
            if (jedis.exists(key)) {
                List<String> list = jedis.lrange(key, 0, -1);
                List<T> result = new ArrayList<>(list.size());
                for (String s : list) {
                    if (isNull(s)) {
                        continue;
                    }
                    result.add(JsonUtil.parseObject(s, clz));
                }
                return result;
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
        return null;
    }

    /**
     * 设置rpush List缓存.
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public <T> void setRpushList(String key, List<T> value, int cacheSeconds) {
        Jedis jedis = null;
        try {
            assertNull(key);
            if (value == null || value.size() == 0) {
                return;
            }
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            List<String> param = new ArrayList<>(value.size());
            for (T o : value) {
                if (o == null) {
                    continue;
                }
                param.add(JsonUtil.toJSONString(o));
            }
            jedis.rpush(key, param.toArray(new String[param.size()]));
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
    }

    /**
     * 向rpush List缓存中添加值.
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> void addRpushList(String key, T... value) {
        Jedis jedis = null;
        try {
            assertNull(key);
            if (value == null || value.length == 0) {
                return;
            }
            jedis = getResource();
            List<String> param = new ArrayList<>(value.length);
            for (T o : value) {
                if (o == null) {
                    continue;
                }
                param.add(JsonUtil.toJSONString(o));
            }
            jedis.rpush(key, param.toArray(new String[param.size()]));
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
    }

    /**
     * 删除缓存.
     *
     * @param key 键
     * @return
     */
    public void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
        } catch (Exception e) {
            outputError(e);
        } finally {
            releaseResource(jedis);
        }
    }

    /**
     * 获取资源.
     *
     * @return Jedis资源
     * @throws JedisException
     */
    public Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            throw e;
        }
        return jedis;
    }

    public void outputError(Exception err) {
        if (err == null) {
            return;
        }
        err.printStackTrace();
    }

    /**
     * 释放资源.
     *
     * @param jedis Jedis资源
     */
    public void releaseResource(Jedis jedis) {
        if (jedis == null) {
            return;
        }
        jedis.close();
    }

    public void assertNull(String key) throws Exception {
        if (key == null || key.length() == 0) {
            throw new Exception("参数不能为空");
        }
    }

    public boolean isNull(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

}
