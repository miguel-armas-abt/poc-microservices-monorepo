package com.demo.bbq.business.diningroomorder.infrastructure.mapper;

import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.MenuOrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOrderMapper {

  MenuOrderEntity fromRequestToEntity(MenuOrderRequest menuOrderRequest, Long tableId);
}