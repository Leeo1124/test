package com.leeo.cache;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Base64Utils;

import com.google.common.base.Joiner;

/**
 * 功能说明：自定义spring的cache的实现，参考cache包实现
 * 
 */
public class MemcachedCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(MemcachedCache.class);

    /**
     * 缓存的别名
     */
    private String name;
    /**
     * memcached客户端
     */
    private MemcachedClient client;
    /**
     * 缓存过期时间，默认是1小时
     * 自定义的属性
     */
    private int exp = 3600;
    /**
     * 是否对key进行base64加密
     */
    private boolean base64Key = false;
    /**
     * 前缀名
     */
    private String prefix;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.client;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object object = null;
        try {
            object = this.client.get(handleKey(objectToString(key)));
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }

        return (object != null ? new SimpleValueWrapper(object) : null);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        try {
            Object object = this.client.get(handleKey(objectToString(key)));
            return (T) object;
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }

        return (T) null;
    }

    @Override
    public void put(Object key, Object value) {
        if (value == null) {
//            this.evict(key);
            return;
        }

        try {
            this.client.set(handleKey(objectToString(key)), this.exp, value);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        this.put(key, value);
        return this.get(key);
    }

    @Override
    public void evict(Object key) {
        try {
            this.client.delete(handleKey(objectToString(key)));
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        try {
            this.client.flushAll();
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (MemcachedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public MemcachedClient getClient() {
        return this.client;
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setBase64Key(boolean base64Key) {
        this.base64Key = base64Key;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 处理key
     * @param key
     * @return
     */
    private String handleKey(final String key) {
        if (this.base64Key) {
            return Joiner.on(EMPTY_SEPARATOR).skipNulls().join(this.prefix, Base64Utils.encode(key.getBytes()));
        }

        return Joiner.on(EMPTY_SEPARATOR).skipNulls().join(this.prefix, key);
    }

    /**
     * 转换key，去掉空格
     * @param object
     * @return
     */
    private static String objectToString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return StringUtils.replace((String) object, " ", "_");
        } else {
            return object.toString();
        }
    }

    private static final String EMPTY_SEPARATOR = "";

}