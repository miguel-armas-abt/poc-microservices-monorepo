package com.demo.bbq.entrypoint.menu.repository.menu;

import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends CrudRepository<MenuEntity, Long> {

  List<MenuEntity> findAll();

  Optional<MenuEntity> findById(Long id);

  Optional<MenuEntity> findByProductCode(String productCode);

  List<MenuEntity> findByCategory(String category);

  MenuEntity save(MenuEntity menuEntity);

  void deleteByProductCode(String productCode);
}
