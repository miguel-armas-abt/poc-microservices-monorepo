package com.demo.bbq.business.menu.infrastructure.mapper;

import com.demo.bbq.business.menu.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOption fromEntityToResponse(MenuOptionEntity menuOption);
  MenuOptionEntity fromRequestToEntity(MenuOptionRequest menuOption);

  @Mapping(target = "productCode", source = "product.code")
  MenuOption fromProductToResponse(MenuOptionEntity menuOption, ProductDto product);
}
