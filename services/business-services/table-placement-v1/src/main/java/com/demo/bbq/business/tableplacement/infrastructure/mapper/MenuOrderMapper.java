package com.demo.bbq.business.tableplacement.infrastructure.mapper;

import com.demo.bbq.business.tableplacement.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.domain.model.response.MenuOrder;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.entity.MenuOrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOrderMapper {

  MenuOrderEntity fromDtoToEntity(MenuOrder menuOrder, Long tableId);

  MenuOrder fromRequestToDto(MenuOrderRequest menuOrder);
}
