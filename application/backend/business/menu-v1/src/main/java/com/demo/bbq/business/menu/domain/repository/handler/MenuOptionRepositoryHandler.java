package com.demo.bbq.business.menu.domain.repository.handler;

import com.demo.bbq.business.menu.domain.exception.MenuOptionException;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponse;
import com.demo.bbq.business.menu.domain.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menu.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.restclient.product.ProductApi;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MenuOptionRepositoryHandler {

  private final String PRODUCT_SCOPE = "MENU";
  private final ProductApi productApi;
  private final MenuOptionRepository menuOptionRepository;
  private final MenuOptionMapper menuOptionMapper;

  public List<MenuOptionResponse> findAll() {
    Map<String, MenuOptionEntity> menuOptionMap = menuOptionRepository.findAll().stream()
        .collect(Collectors.toMap(MenuOptionEntity::getProductCode, Function.identity()));

    return productApi.findByScope(PRODUCT_SCOPE).blockingGet().stream()
        .map(product -> menuOptionMapper.fromProductToResponse(menuOptionMap.get(product.getCode()), product))
        .collect(Collectors.toList());
  }

  public void save(MenuOptionSaveRequest menuOption) {
    productApi.save(menuOptionMapper.fromSaveRequestToProduct(menuOption, PRODUCT_SCOPE)).blockingGet();
    menuOptionRepository.save(menuOptionMapper.fromSaveRequestToEntity(menuOption));
  }

  public void update(String productCode, MenuOptionUpdateRequest menuOption) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          MenuOptionEntity menuOptionEntity = menuOptionMapper.fromUpdateRequestToEntity(menuOption, productCode);
          menuOptionEntity.setId(menuOptionFound.getId());
          return menuOptionRepository.save(menuOptionEntity);
        })
        .orElseThrow(MenuOptionException.ERROR0000::buildException);
    productApi.update(productCode, menuOptionMapper.fromUpdateRequestToProduct(menuOption, PRODUCT_SCOPE)).blockingGet();
  }

  @Transactional
  public void deleteByProductCode(String productCode) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          menuOptionRepository.deleteByProductCode(productCode);
          return menuOptionFound;
        })
        .orElseThrow(MenuOptionException.ERROR0000::buildException);
    productApi.delete(productCode).blockingGet();
  }

}