package com.demo.bbq.commons.config;

import com.demo.bbq.commons.cache.CustomRedisCacheManager;
import com.demo.bbq.commons.properties.ApplicationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@EnableCaching
@Configuration
public class CacheConfig {

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ApplicationProperties properties) {
    return new CustomRedisCacheManager(redisConnectionFactory, properties);
  }
}