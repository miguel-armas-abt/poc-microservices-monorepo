package com.demo.bbq.application.service;

import com.demo.bbq.application.enums.MenuCategory;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.MenuOptionRepositoryHandler;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MenuOptionServiceImpl implements MenuOptionService {

  private final MenuOptionRepositoryHandler menuOptionRepositoryHandler;

  private final ServiceConfigurationProperties properties;

  @Override
  public Multi<MenuOptionResponseDTO> findByCategory(String categoryCode, Map<String, String> headers) {
    properties.errorMessages().get().messages();

    return (Optional.ofNullable(categoryCode).isEmpty()
        ? menuOptionRepositoryHandler.findAll(headers)
        : this.validateMenuOptionAndFindByCategory(categoryCode, headers))
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .flatMap(item -> Multi.createFrom().item(item));
  }

  private Uni<List<MenuOptionResponseDTO>> validateMenuOptionAndFindByCategory(String categoryCode, Map<String, String> headers) {
    MenuCategory.validateCategory.accept(categoryCode);
    return menuOptionRepositoryHandler.findAll(headers)
        .map(menuOptions -> menuOptions.stream().filter(menu -> menu.getCategory().equals(categoryCode))
              .collect(Collectors.toList()));
  }

  @Override
  public Uni<MenuOptionResponseDTO> findByProductCode(String productCode, Map<String, String> headers) {
    return menuOptionRepositoryHandler.findAll(headers)
        .map(menuOptions -> menuOptions.stream().filter(menuOption -> productCode.equals(menuOption.getProductCode())).findFirst().get());
  }

  @Override
  public Uni<Void> save(MenuOptionSaveRequestDTO menuOptionRequest, Map<String, String> headers) {
    return menuOptionRepositoryHandler.save(menuOptionRequest, headers);
  }

  @Override
  public Uni<Void> update(MenuOptionUpdateRequestDTO menuOptionRequest, String productCode, Map<String, String> headers) {
    return menuOptionRepositoryHandler.update(productCode, menuOptionRequest, headers);
  }

  @Override
  public Uni<Void> deleteByProductCode(String productCode, Map<String, String> headers) {
    return menuOptionRepositoryHandler.deleteByProductCode(productCode, headers);
  }

}
