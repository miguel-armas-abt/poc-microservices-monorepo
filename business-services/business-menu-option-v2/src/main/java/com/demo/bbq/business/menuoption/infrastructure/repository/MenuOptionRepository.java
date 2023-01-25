package com.demo.bbq.business.menuoption.infrastructure.repository;

import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MenuOptionRepository implements PanacheRepositoryBase<MenuOptionEntity, Long> {

  public Uni<MenuOptionEntity> findMenuOptionById(Long id) {
    return this.<MenuOptionEntity>findById(id);
  }

  public Multi<MenuOptionEntity> findByCategory(String categoryCode) {
    return this.<MenuOptionEntity>find("category", categoryCode)
        .stream();
  }

  public Multi<MenuOptionEntity> findAllMenuOptions() {
    return this.<MenuOptionEntity>findAll()
        .stream();
  }

  public Uni<MenuOptionEntity> saveMenuOption(MenuOptionEntity menuOptionEntity) {
    return Panache.withTransaction(menuOptionEntity::<MenuOptionEntity>persist);
  }

  public Uni<MenuOptionEntity> update(MenuOptionEntity menuOptionEntity, Long id) {
    return Panache.withTransaction(() -> this.<MenuOptionEntity>findById(id)
        .onItem()
        .ifNotNull()
        .invoke(menuOptionEntityFound -> {
          menuOptionEntityFound.setDescription(menuOptionEntity.getDescription());
          menuOptionEntityFound.setCategory(menuOptionEntity.getCategory());
          menuOptionEntityFound.setPrice(menuOptionEntity.getPrice());
          menuOptionEntityFound.setActive(menuOptionEntity.isActive());
        }));
  }

  public Uni<Boolean> deleteMenuOptionById(Long id) {
    return Panache.withTransaction(() -> this.deleteById(id));
  }
}
