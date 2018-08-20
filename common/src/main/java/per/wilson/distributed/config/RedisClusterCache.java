package per.wilson.distributed.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

/**
 * RedisClusterCache
 *
 * @author Wilson
 * @date 18-8-19
 */
public class RedisClusterCache extends AbstractValueAdaptingCache {
    private JedisClusterUtil jedisClusterUtil;

    private final String name;

    public RedisClusterCache(boolean allowNullValues, JedisClusterUtil jedisClusterUtil, String name) {
        super(allowNullValues);
        this.jedisClusterUtil = jedisClusterUtil;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.jedisClusterUtil;
    }

    @Override
    protected Object lookup(Object key) {
        return jedisClusterUtil.get(key instanceof String ? (String) key : key.toString());
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper result = get(key);
        if (result != null) {
            return (T) result.get();
        }
        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        jedisClusterUtil.put(key instanceof String ? (String) key : key.toString(), JSONObject.toJSONString(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }
        Object result = jedisClusterUtil.putIfAbsent((String) key, value);
        if (result == null) {
            return null;
        }
        return new SimpleValueWrapper(fromStoreValue(result));
    }

    @Override
    public void evict(Object key) {
        jedisClusterUtil.del((String) key);
    }

    @Override
    public void clear() {
        jedisClusterUtil.clear();
    }

    @Nullable
    protected Object preProcessCacheValue(@Nullable Object value) {
        if (value != null) {
            return value;
        }
        return isAllowNullValues() ? NullValue.INSTANCE : null;
    }


    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {
        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }
}
