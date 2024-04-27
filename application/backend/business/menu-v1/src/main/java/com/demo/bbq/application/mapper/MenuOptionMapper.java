package com.demo.bbq.application.mapper;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductUpdateRequestWrapper;
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
