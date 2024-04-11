package com.demo.bbq.business.menu.application.mapper;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menu.domain.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuOptionMapper {

  MenuOptionEntity toEntity(MenuOptionSaveRequestDTO menuOption);

  MenuOptionEntity toEntity(MenuOptionUpdateRequestDTO menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuOptionResponseDTO toResponseDTO(MenuOptionEntity menuOption, ProductResponseWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuOptionSaveRequestDTO menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuOptionUpdateRequestDTO menuOption, String scope);
}
