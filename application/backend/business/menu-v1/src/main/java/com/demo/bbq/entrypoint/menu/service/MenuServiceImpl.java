package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.enums.MenuCategory;
import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryHandler;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
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
public class MenuServiceImpl implements MenuService {

  private final MenuRepositoryHandler menuRepositoryHandler;

  @Override
  public List<MenuResponseDTO> findByCategory(HttpServletRequest servletRequest, String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
          ? menuRepositoryHandler.findAll(servletRequest)
          : this.validateMenuOptionAndFindByCategory(servletRequest, categoryCode);
  }

  private List<MenuResponseDTO> validateMenuOptionAndFindByCategory(HttpServletRequest servletRequest, String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuRepositoryHandler.findAll(servletRequest)
        .stream()
        .filter(menu -> menu.getCategory().equals(categoryCode))
        .collect(Collectors.toList());
  }

  @Override
  public MenuResponseDTO findByProductCode(HttpServletRequest servletRequest, String productCode) {
    return menuRepositoryHandler.findAll(servletRequest)
        .stream()
        .filter(menuOption -> productCode.equals(menuOption.getProductCode()))
        .findFirst()
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
  }

  @Override
  public void save(HttpServletRequest servletRequest, MenuSaveRequestDTO menuOption) {
    menuRepositoryHandler.save(servletRequest, menuOption);
  }

  @Override
  public void update(HttpServletRequest servletRequest, String productCode, MenuUpdateRequestDTO menuOption) {
    menuRepositoryHandler.update(servletRequest, productCode, menuOption);
  }

  @Override
  public void deleteByProductCode(HttpServletRequest servletRequest, String productCode) {
    menuRepositoryHandler.deleteByProductCode(servletRequest, productCode);
  }

}
