package io.project.app.common.api;

public interface CacheService {

    <T> T get(String key, Class<T> clazz);

    <T> boolean set(String key, T value);

    <T> boolean set(String key, T value, int expireSeconds);

    boolean exists(String key);

    long incr(String key);

    long decr(String key);

    boolean delete(String key);
}
