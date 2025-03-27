package com.demo.poc.entrypoint.menu.service;

import com.demo.poc.commons.custom.exceptions.InvalidMenuCategoryException;
import com.demo.poc.commons.custom.exceptions.MenuOptionNotFoundException;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.poc.entrypoint.menu.mapper.MenuResponseMapper;
import com.demo.poc.entrypoint.menu.repository.MenuAndProductCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuServiceImpl implements MenuService {

  private final MenuAndProductCache menuAndProductCache;
  private final MenuResponseMapper mapper;
  private final ApplicationProperties properties;

  @Override
  public List<MenuResponseDTO> findByCategory(Map<String, String> headers, String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
        ? this.findAll(headers)
        : this.validateMenuOptionAndFindByCategory(headers, categoryCode);
  }

  private List<MenuResponseDTO> validateMenuOptionAndFindByCategory(Map<String, String> headers, String categoryCode) {
    validateCategory(categoryCode);
    return this.findAll(headers)
        .stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuResponseDTO findByProductCode(Map<String, String> headers, String productCode) {
    return this.findAll(headers)
        .stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .findFirst()
        .orElseThrow(MenuOptionNotFoundException::new);
  }

  @Override
  public void save(Map<String, String> headers, MenuSaveRequestDTO menuOption) {
    menuAndProductCache.save(headers, menuOption);
  }

  @Override
  public void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption) {
    menuAndProductCache.update(headers, productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(Map<String, String> headers, String productCode) {
    menuAndProductCache.deleteByProductCode(headers, productCode);
  }

  private List<MenuResponseDTO> findAll(Map<String, String> headers) {
    return mapper.toResponseDTO(menuAndProductCache.findAll(headers));
  }

  private void validateCategory(String category) {
    Set<String> categories = properties.getFeatures().getMenuCategories();
    if (!categories.contains(category.toLowerCase())) {
      throw new InvalidMenuCategoryException();
    }
  }

}
