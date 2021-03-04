
package com.zz.core.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 *     Redis Template 配置
 * </p>
 */
@Configuration
public class RedisTemplateConfig {

    @Bean("redisTemplate")
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 自定义的string序列化器和fastjson序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // jackson 序列化器
        //GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // fastjson 序列化器
        FastJsonRedisSerializer jsonRedisSerializer = new FastJsonRedisSerializer(Object.class);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);

        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jsonRedisSerializer);

        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
