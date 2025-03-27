package com.demo.poc.entrypoint.menu.repository;

import com.demo.poc.commons.custom.exceptions.MenuOptionNotFoundException;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.poc.entrypoint.menu.mapper.MenuRequestMapper;
import com.demo.poc.entrypoint.menu.repository.menu.MenuRepository;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.poc.entrypoint.menu.repository.product.ProductRepository;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MenuAndProductJoiner {

  private final String PRODUCT_SCOPE = "MENU";
  private final ProductRepository productRepository;
  private final MenuRepository menuRepository;
  private final MenuRequestMapper mapper;

  public Map<MenuEntity, ProductResponseWrapper> findAll(Map<String, String> headers) {
    Map<String, MenuEntity> menuOptionMap = menuRepository.findAll().stream()
        .collect(Collectors.toMap(MenuEntity::getProductCode, Function.identity()));

    Map<MenuEntity, ProductResponseWrapper> menuAndProductMap = new HashMap<>();
    productRepository.findByScope(headers, PRODUCT_SCOPE)
        .forEach(product -> menuAndProductMap.put(menuOptionMap.get(product.getCode()), product));

    return menuAndProductMap;
  }

  public void save(Map<String, String> headers, MenuSaveRequestDTO menuOption) {
    productRepository.save(headers, mapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
    menuRepository.save(mapper.toEntity(menuOption));
  }

  public void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption) {
    menuRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          MenuEntity menuEntity = mapper.toEntity(menuOption, productCode);
          menuEntity.setId(menuOptionFound.getId());
          return menuRepository.save(menuEntity);
        })
        .orElseThrow(MenuOptionNotFoundException::new);
    productRepository.update(headers, productCode, mapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
  }

  @Transactional
  public void deleteByProductCode(Map<String, String> headers, String productCode) {
    menuRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          menuRepository.deleteByProductCode(productCode);
          return menuOptionFound;
        })
        .orElseThrow(MenuOptionNotFoundException::new);
    productRepository.delete(headers, productCode);
  }

}