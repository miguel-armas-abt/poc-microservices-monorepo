package com.demo.bbq.entrypoint.menu.repository;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuAndProductCache {

  private static final String CACHE_NAME = "menu-and-products";

  private final MenuAndProductJoiner joiner;

  @Cacheable(value = CACHE_NAME, key = "#root.methodName")
  public Map<MenuEntity, ProductResponseWrapper> findAll(Map<String, String> headers) {
      return joiner.findAll(headers);
  }

  @CacheEvict(value = CACHE_NAME, allEntries = true)
  public void save(Map<String, String> headers, MenuSaveRequestDTO menuOption) {
    joiner.save(headers, menuOption);
  }

  @CacheEvict(value = CACHE_NAME, allEntries = true)
  public void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption) {
    joiner.update(headers, productCode, menuOption);
  }

  @CacheEvict(value = CACHE_NAME, allEntries = true)
  public void deleteByProductCode(Map<String, String> headers, String productCode) {
    joiner.deleteByProductCode(headers, productCode);
  }
}
