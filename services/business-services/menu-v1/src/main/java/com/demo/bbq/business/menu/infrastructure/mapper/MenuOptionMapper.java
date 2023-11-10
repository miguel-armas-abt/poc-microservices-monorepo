package com.demo.bbq.business.menu.infrastructure.mapper;

import com.demo.bbq.business.menu.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOption fromEntityToResponse(MenuOptionEntity menuOptionEntity);
  MenuOptionEntity fromRequestToEntity(MenuOptionRequest menuOption);
}
