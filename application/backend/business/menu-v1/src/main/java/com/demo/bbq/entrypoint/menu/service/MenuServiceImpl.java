package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuServiceImpl implements MenuService {

  private final MenuRepositoryJoiner menuRepositoryJoiner;
  private final ApplicationProperties properties;

  @Override
  public List<MenuResponseDTO> findByCategory(Map<String, String> headers, String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
          ? menuRepositoryJoiner.findAll(headers)
          : this.validateMenuOptionAndFindByCategory(headers, categoryCode);
  }

  private List<MenuResponseDTO> validateMenuOptionAndFindByCategory(Map<String, String> headers, String categoryCode) {
    validateCategory(categoryCode);
    return menuRepositoryJoiner.findAll(headers)
        .stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuResponseDTO findByProductCode(Map<String, String> headers, String productCode) {
    return menuRepositoryJoiner.findAll(headers)
        .stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .findFirst()
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
  }

  @Override
  public void save(Map<String, String> headers, MenuSaveRequestDTO menuOption) {
    menuRepositoryJoiner.save(headers, menuOption);
  }

  @Override
  public void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption) {
    menuRepositoryJoiner.update(headers, productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(Map<String, String> headers, String productCode) {
    menuRepositoryJoiner.deleteByProductCode(headers, productCode);
  }

  private void validateCategory(String category) {
    Set<String> categories = properties.getFeatures().getMenuCategories();
    if (!categories.contains(category.toLowerCase())) {
      throw new BusinessException("The menu category is not defined");
    }
  }

}
