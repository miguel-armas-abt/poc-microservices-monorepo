package com.demo.bbq.business.menu.infrastructure.repository.cache.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;

@EnableCaching
public class CacheConfig {

//  @Bean
//  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//        .entryTtl(Duration.ofMinutes(5));
//
//    return RedisCacheManager.builder(connectionFactory)
//        .cacheDefaults(cacheConfiguration)
//        .build();
//  }
}
