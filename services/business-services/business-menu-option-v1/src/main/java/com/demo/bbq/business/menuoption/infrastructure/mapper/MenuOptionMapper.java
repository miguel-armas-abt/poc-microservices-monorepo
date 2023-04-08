package com.demo.bbq.business.menuoption.infrastructure.mapper;

import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOption fromEntityToResponse(MenuOptionEntity menuOptionEntity);
  MenuOptionEntity fromRequestToEntity(MenuOptionRequest menuOption);
}
