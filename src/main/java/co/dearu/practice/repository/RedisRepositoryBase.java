package co.dearu.practice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepositoryBase {
    @Autowired
    protected RedisTemplate redisTemplate;
}
