package com.demo.bbq.business.menu.infrastructure.repository.cache.service;

import com.demo.bbq.business.menu.domain.model.response.MenuOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MenuOptionCache {

//  private final String CACHE_NAME = "menu.options";
//  private final String CACHE_KEY = "ALL";
//  private final CacheManager cacheManager;
//
//  @CachePut(value = CACHE_NAME, key = CACHE_KEY)
//  public List<MenuOption> put(List<MenuOption> menuOption) {
//    return menuOption;
//  }
//
//  public Optional<MenuOption> getAll() {
//    Cache menuOptionCache = getMenuOptionCache();
//    return Optional.ofNullable(menuOptionCache)
//        .map(cache -> cache.get(CACHE_KEY, ValueWrapper.class))
//        .map(ValueWrapper::get)
//        .map(menuOption -> (MenuOption) menuOption);
//  }
//
//  private Cache getMenuOptionCache() {
//    return cacheManager.getCache(CACHE_NAME);
//  }

}
