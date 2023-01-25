package com.demo.bbq.business.diningroomorder.infrastructure.repository.database;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TableRepository extends CrudRepository<DiningRoomTableEntity, Long> {

  Optional<DiningRoomTableEntity> findByTableNumber(Integer tableNumber);

}