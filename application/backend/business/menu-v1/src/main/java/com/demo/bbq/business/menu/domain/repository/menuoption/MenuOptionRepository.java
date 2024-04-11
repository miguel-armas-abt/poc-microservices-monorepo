package com.demo.bbq.business.menu.domain.repository.menuoption;

import com.demo.bbq.business.menu.domain.repository.menuoption.entity.MenuOptionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOptionRepository extends CrudRepository<MenuOptionEntity, Long> {

  List<MenuOptionEntity> findAll();

  Optional<MenuOptionEntity> findById(Long id);

  Optional<MenuOptionEntity> findByProductCode(String productCode);

  List<MenuOptionEntity> findByCategory(String category);

  MenuOptionEntity save(MenuOptionEntity menuOptionEntity);

  void deleteByProductCode(String productCode);
}
