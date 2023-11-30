package com.demo.bbq.business.menuoption.application.service.impl;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.domain.catalog.MenuCategory;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.business.menuoption.infrastructure.repository.handler.MenuOptionRepositoryHandler;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MenuOptionServiceImpl implements MenuOptionService {

  @Inject
  MenuOptionRepositoryHandler menuOptionRepositoryHandler;

  @Override
  public Uni<List<MenuOption>> findByCategory(String categoryCode) {
    return Optional.ofNullable(categoryCode).isEmpty()
        ? menuOptionRepositoryHandler.findAll()
        : this.validateMenuOptionAndFindByCategory(categoryCode);
  }

  private Uni<List<MenuOption>> validateMenuOptionAndFindByCategory(String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menu -> menu.getCategory().equals(categoryCode))
              .collect(Collectors.toList()));
  }

  @Override
  public Uni<MenuOption> findByProductCode(String productCode) {
    return menuOptionRepositoryHandler.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menuOption -> productCode.equals(menuOption.getProductCode())).findFirst().get());
  }

  @Override
  public Uni<Void> save(MenuOptionSaveRequest menuOptionRequest) {
    return menuOptionRepositoryHandler.save(menuOptionRequest);
  }

  @Override
  public Uni<Void> update(MenuOptionUpdateRequest menuOptionRequest, String productCode) {
    return menuOptionRepositoryHandler.update(productCode, menuOptionRequest);
  }

  @Override
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuOptionRepositoryHandler.deleteByProductCode(productCode);
  }

}
