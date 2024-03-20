package com.demo.bbq.business.menu.application.mapper;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.application.dto.response.MenuOption;
import com.demo.bbq.business.menu.domain.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductWrapper;
import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductSaveRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductUpdateRequestWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOptionEntity fromSaveRequestToEntity(MenuOptionSaveRequest menuOption);

  MenuOptionEntity fromUpdateRequestToEntity(MenuOptionUpdateRequest menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuOption fromProductToResponse(MenuOptionEntity menuOption, ProductWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper fromSaveRequestToProduct(MenuOptionSaveRequest menuOption, String scope);

  ProductUpdateRequestWrapper fromUpdateRequestToProduct(MenuOptionUpdateRequest menuOption, String scope);
}
