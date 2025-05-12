package com.demo.poc.commons.custom.cache;

import com.demo.poc.commons.custom.properties.ApplicationProperties;
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