package com.demo.bbq.business.diningroomorder.infrastructure.mapper;

import com.demo.bbq.business.diningroomorder.domain.model.dto.DiningRoomOrderDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiningRoomOrderMapper {

  @Mapping(target = "id", source = "id")
  DiningRoomOrderDto fromEntityToDto(DiningRoomTableEntity diningRoomOrder);
}
