package com.demo.bbq.business.menuoption.infrastructure.repository.database;

import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MenuOptionRepository implements PanacheRepositoryBase<MenuOptionEntity, Long> {

  public Uni<MenuOptionEntity> findByProductCode(String productCode) {
    return this.<MenuOptionEntity>find("product_code", productCode)
        .firstResult();
  }

  public Multi<MenuOptionEntity> findByScope(String scope) {
    return this.<MenuOptionEntity>find("scope", scope)
        .stream();
  }

  public Multi<MenuOptionEntity> findAllMenuOptions() {
    return this.<MenuOptionEntity>findAll()
        .stream();
  }

  //error during automatic ID generation
  public Uni<MenuOptionEntity> saveMenuOption(MenuOptionEntity menuOptionEntity) {
    return Panache.withTransaction(() -> this.persist(menuOptionEntity));
  }

  public Uni<MenuOptionEntity> update(MenuOptionEntity menuOptionEntity, String productCode) {
    return Panache.withTransaction(() -> this.<MenuOptionEntity>findByProductCode(productCode)
        .onItem()
        .ifNotNull()
        .invoke(menuOptionEntityFound -> {
          menuOptionEntityFound.setProductCode(menuOptionEntity.getProductCode());
          menuOptionEntityFound.setDescription(menuOptionEntity.getDescription());
          menuOptionEntityFound.setCategory(menuOptionEntity.getCategory());
        }));
  }

  public Uni<Void> deleteByProductCode(String productCode) {
    return Panache.withTransaction(() -> this.delete("product_code = ?1", productCode))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }
}
