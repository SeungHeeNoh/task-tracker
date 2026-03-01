package com.hohohehe.tasktracker.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(new JedisConnectionFactory());

        // Key는 String 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // Value는 JSON 직렬화 (GenericJackson2JsonRedisSerializer 사용)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
