package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.application.enums.MenuCategory;
import com.demo.bbq.repository.MenuOptionRepositoryHandler;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
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
  public List<MenuOptionResponseDTO> findByCategory(HttpServletRequest servletRequest, String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
          ? menuOptionRepositoryHandler.findAll(servletRequest)
          : this.validateMenuOptionAndFindByCategory(servletRequest, categoryCode);
  }

  private List<MenuOptionResponseDTO> validateMenuOptionAndFindByCategory(HttpServletRequest servletRequest, String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll(servletRequest)
        .stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuOptionResponseDTO findByProductCode(HttpServletRequest servletRequest, String productCode) {
    return menuOptionRepositoryHandler.findAll(servletRequest)
        .stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .findFirst()
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
  }

  @Override
  public void save(HttpServletRequest servletRequest, MenuOptionSaveRequestDTO menuOption) {
    menuOptionRepositoryHandler.save(servletRequest, menuOption);
  }

  @Override
  public void update(HttpServletRequest servletRequest, String productCode, MenuOptionUpdateRequestDTO menuOption) {
    menuOptionRepositoryHandler.update(servletRequest, productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(HttpServletRequest servletRequest, String productCode) {
    menuOptionRepositoryHandler.deleteByProductCode(servletRequest, productCode);
  }

}
