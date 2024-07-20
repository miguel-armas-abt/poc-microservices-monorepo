package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.config.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final MenuProductMatcher menuProductMatcher;
  private final ApplicationProperties properties;

  @Override
  public Multi<MenuResponseDTO> findByCategory(String categoryCode) {
    return (Optional.ofNullable(categoryCode).isEmpty()
        ? menuProductMatcher.findAll()
        : this.validateMenuOptionAndFindByCategory(categoryCode))
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .flatMap(item -> Multi.createFrom().item(item));
  }

  private Uni<List<MenuResponseDTO>> validateMenuOptionAndFindByCategory(String categoryCode) {
    validateCategory(categoryCode);
    return menuProductMatcher.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menu -> menu.getCategory().equals(categoryCode))
              .collect(Collectors.toList()));
  }

  @Override
  public Uni<MenuResponseDTO> findByProductCode(String productCode) {
    return menuProductMatcher.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menuOption -> productCode.equals(menuOption.getProductCode())).findFirst().get());
  }

  @Override
  public Uni<Void> save(MenuSaveRequestDTO menuOptionRequest) {
    return menuProductMatcher.save(menuOptionRequest);
  }

  @Override
  public Uni<Void> update(MenuUpdateRequestDTO menuOptionRequest, String productCode) {
    return menuProductMatcher.update(productCode, menuOptionRequest);
  }

  @Override
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuProductMatcher.deleteByProductCode(productCode);
  }

  private void validateCategory(String category) {
    Set<String> categories = properties.features().menuCategories();
    if (!categories.contains(category.toLowerCase())) {
      throw new BusinessException("The menu category is not defined");
    }
  }

}
