package com.demo.bbq.business.menuoption.application.service.impl;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.infrastructure.repository.MenuOptionRepository;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Optional;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MenuOptionServiceImpl implements MenuOptionService {

  @Inject
  MenuOptionRepository menuOptionRepository;

  @Override
  public Uni<MenuOption> findById(Long id) {
    return menuOptionRepository.findById(id)
        .map(fromEntityToDomain);
  }

  @Override
  public Multi<MenuOption> findByCategory(String categoryCode) {
    return (Optional.ofNullable(categoryCode).isEmpty()
        ? menuOptionRepository.findAllMenuOptions()
        : menuOptionRepository.findByCategory(categoryCode))
        .map(fromEntityToDomain);
  }

  @Override
  public Uni<Long> save(MenuOptionRequest menuOptionRequest) {
    return menuOptionRepository.saveMenuOption(fromRequestToEntity.apply(menuOptionRequest))
        .invoke(x -> log.info("saved: " + x.toString()))
        .map(fromEntityToDomain)
        .map(MenuOption::getId);
  }

  @Override
  public Uni<Long> update(MenuOptionRequest menuOptionRequest, Long id) {
    return menuOptionRepository.update(fromRequestToEntity.apply(menuOptionRequest), id)
        .map(menuOptionEntity -> menuOptionEntity.id);
  }

  @Override
  public Uni<Boolean> deleteById(Long id) {
    return menuOptionRepository.deleteMenuOptionById(id);
  }

  private static final Function<MenuOptionEntity, MenuOption>
      fromEntityToDomain = menuOptionEntity -> MenuOption.builder()
      .id(menuOptionEntity.id)
      .description(menuOptionEntity.getDescription())
      .category(menuOptionEntity.getCategory())
      .price(menuOptionEntity.getPrice())
      .active(menuOptionEntity.isActive())
      .build();

  private static final Function<MenuOptionRequest, MenuOptionEntity>
      fromRequestToEntity = menuOptionRequest -> MenuOptionEntity.builder()
      .description(menuOptionRequest.getDescription())
      .category(menuOptionRequest.getCategory())
      .price(menuOptionRequest.getPrice())
      .active(menuOptionRequest.isActive())
      .build();

}
