package com.demo.bbq.business.menu.infrastructure.mapper;

import com.demo.bbq.business.menu.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductSaveRequestDto;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOptionEntity fromSaveRequestToEntity(MenuOptionSaveRequest menuOption);

  MenuOptionEntity fromUpdateRequestToEntity(MenuOptionUpdateRequest menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuOption fromProductToResponse(MenuOptionEntity menuOption, ProductDto product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestDto fromSaveRequestToProduct(MenuOptionSaveRequest menuOption, String scope);

  ProductUpdateRequestDto fromUpdateRequestToProduct(MenuOptionUpdateRequest menuOption, String scope);
}
