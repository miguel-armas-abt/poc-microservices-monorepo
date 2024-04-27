package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.application.catalog.MenuCategory;
import com.demo.bbq.repository.MenuOptionRepositoryHandler;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
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
  public List<MenuOptionResponseDTO> findByCategory(String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
          ? menuOptionRepositoryHandler.findAll()
          : this.validateMenuOptionAndFindByCategory(categoryCode);
  }

  private List<MenuOptionResponseDTO> validateMenuOptionAndFindByCategory(String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll().stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuOptionResponseDTO findByProductCode(String productCode) {
    return menuOptionRepositoryHandler.findAll().stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .peek(menuOption -> log.info("{}", new Gson().toJson(menuOption)))
        .findFirst()
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
  }

  @Override
  public void save(MenuOptionSaveRequestDTO menuOption) {
    menuOptionRepositoryHandler.save(menuOption);
  }

  @Override
  public void update(String productCode, MenuOptionUpdateRequestDTO menuOption) {
    menuOptionRepositoryHandler.update(productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(String productCode) {
    menuOptionRepositoryHandler.deleteByProductCode(productCode);
  }

}
