package com.demo.bbq.business.menu.application.service;

import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponse;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.application.catalog.MenuCategory;
import com.demo.bbq.business.menu.domain.exception.MenuOptionException;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.domain.repository.handler.MenuOptionRepositoryHandler;
import com.demo.bbq.support.logstash.Markers;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuOptionServiceImpl implements MenuOptionService {

  private final MenuOptionRepositoryHandler menuOptionRepositoryHandler;

  @Override
  public List<MenuOptionResponse> findByCategory(String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
          ? menuOptionRepositoryHandler.findAll()
          : this.validateMenuOptionAndFindByCategory(categoryCode);
  }

  private List<MenuOptionResponse> validateMenuOptionAndFindByCategory(String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll().stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuOptionResponse findByProductCode(String productCode) {
    return menuOptionRepositoryHandler.findAll().stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .peek(menuOption -> log.info(Markers.SENSITIVE_JSON, "findByProductCode: {}", new Gson().toJson(menuOption)))
        .findFirst()
        .orElseThrow(MenuOptionException.ERROR0000::buildException);
  }

  @Override
  public void save(MenuOptionSaveRequest menuOption) {
    menuOptionRepositoryHandler.save(menuOption);
  }

  @Override
  public void update(String productCode, MenuOptionUpdateRequest menuOption) {
    menuOptionRepositoryHandler.update(productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(String productCode) {
    menuOptionRepositoryHandler.deleteByProductCode(productCode);
  }

}
