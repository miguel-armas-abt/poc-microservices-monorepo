package com.demo.bbq.business.menuoption.infrastructure.repository.database;

import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOptionRepository extends CrudRepository<MenuOptionEntity, Long> {

  List<MenuOptionEntity> findAll();

  Optional<MenuOptionEntity> findById(Long id);

  List<MenuOptionEntity> findByCategory(String category);

  MenuOptionEntity save(MenuOptionEntity menuOptionEntity);

  void deleteById(Long id);
}