package com.demo.bbq.business.diningroomorder.infrastructure.mapper;

import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.domain.model.response.MenuOrder;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.MenuOrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOrderMapper {

  MenuOrderEntity fromDomainToEntity(MenuOrder menuOrder, Long tableId);

  MenuOrder fromRequestToDomain(MenuOrderRequest menuOrder);
}
