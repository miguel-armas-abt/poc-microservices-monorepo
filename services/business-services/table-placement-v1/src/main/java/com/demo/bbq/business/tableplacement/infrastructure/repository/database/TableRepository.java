package com.demo.bbq.business.tableplacement.infrastructure.repository.database;

import com.demo.bbq.business.tableplacement.infrastructure.repository.database.entity.TableEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<TableEntity, Long> {

  Optional<TableEntity> findByTableNumber(Integer tableNumber);

}
