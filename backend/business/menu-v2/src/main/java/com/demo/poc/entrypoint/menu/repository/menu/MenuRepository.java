package com.demo.poc.entrypoint.menu.repository.menu;

import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MenuRepository implements PanacheRepositoryBase<MenuEntity, Long> {

  @WithSession
  public Uni<MenuEntity> findByProductCode(String productCode) {
    return this.find("productCode", productCode)
        .firstResult();
  }

  @WithSession
  public Uni<List<MenuEntity>> findByScope(String scope) {
    return this.find("scope", scope)
        .list();
  }

  @WithSession
  public Uni<List<MenuEntity>> findAllMenuOptions() {
    return this.findAll()
        .list();
  }

  @WithTransaction
  //error during automatic ID generation
  public Uni<MenuEntity> saveMenuOption(MenuEntity menuEntity) {
    return Panache.withTransaction(() -> this.persist(menuEntity));
  }

  @WithTransaction
  public Uni<MenuEntity> update(MenuEntity menuEntity, String productCode) {
    return Panache.withTransaction(() -> this.<MenuEntity>findByProductCode(productCode)
        .onItem()
        .ifNotNull()
        .invoke(menuEntityFound -> {
          menuEntityFound.setProductCode(menuEntity.getProductCode());
          menuEntityFound.setDescription(menuEntity.getDescription());
          menuEntityFound.setCategory(menuEntity.getCategory());
        }));
  }

  @WithTransaction
  public Uni<Void> deleteByProductCode(String productCode) {
    return Panache.withTransaction(() -> this.delete("productCode = ?1", productCode))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }
}
