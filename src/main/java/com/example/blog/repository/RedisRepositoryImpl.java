package com.example.blog.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveCode(String key, String email, String code) {
        redisTemplate.opsForHash().put(key, email, code);
    }

    @Override
    public void deleteCode(String key, String email) {
        redisTemplate.opsForHash().delete(key, email);
    }

    @Override
    public Map<Object, Object> findAllCodes(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}
