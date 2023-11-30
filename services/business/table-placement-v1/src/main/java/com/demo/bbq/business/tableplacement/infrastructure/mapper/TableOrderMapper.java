package com.demo.bbq.business.tableplacement.infrastructure.mapper;

import com.demo.bbq.business.tableplacement.domain.model.response.TableOrder;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.entity.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableOrderMapper {

  @Mapping(target = "id", source = "id")
  TableOrder fromEntityToDto(TableEntity diningRoomOrder);
}
