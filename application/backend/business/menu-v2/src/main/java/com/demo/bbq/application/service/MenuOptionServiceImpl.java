package com.demo.bbq.application.service;

import com.demo.bbq.application.enums.MenuCategory;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.repository.MenuOptionRepositoryHandler;
import io.smallrye.mutiny.Multi;
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
  public Multi<MenuOptionResponseDTO> findByCategory(String categoryCode) {
    return (Optional.ofNullable(categoryCode).isEmpty()
        ? menuOptionRepositoryHandler.findAll()
        : this.validateMenuOptionAndFindByCategory(categoryCode))
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .flatMap(item -> Multi.createFrom().item(item));
  }

  private Uni<List<MenuOptionResponseDTO>> validateMenuOptionAndFindByCategory(String categoryCode) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menu -> menu.getCategory().equals(categoryCode))
              .collect(Collectors.toList()));
  }

  @Override
  public Uni<MenuOptionResponseDTO> findByProductCode(String productCode) {
    return menuOptionRepositoryHandler.findAll()
        .map(menuOptions -> menuOptions.stream().filter(menuOption -> productCode.equals(menuOption.getProductCode())).findFirst().get());
  }

  @Override
  public Uni<Void> save(MenuOptionSaveRequestDTO menuOptionRequest) {
    return menuOptionRepositoryHandler.save(menuOptionRequest);
  }

  @Override
  public Uni<Void> update(MenuOptionUpdateRequestDTO menuOptionRequest, String productCode) {
    return menuOptionRepositoryHandler.update(productCode, menuOptionRequest);
  }

  @Override
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuOptionRepositoryHandler.deleteByProductCode(productCode);
  }

}
