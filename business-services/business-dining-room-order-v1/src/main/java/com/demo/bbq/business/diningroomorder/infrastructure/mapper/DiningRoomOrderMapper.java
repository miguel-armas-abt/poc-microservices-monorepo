package com.demo.bbq.business.diningroomorder.infrastructure.mapper;

import com.demo.bbq.business.diningroomorder.domain.model.response.DiningRoomOrder;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiningRoomOrderMapper {

  DiningRoomOrder fromEntityToDomain(DiningRoomTableEntity diningRoomOrder);
}
