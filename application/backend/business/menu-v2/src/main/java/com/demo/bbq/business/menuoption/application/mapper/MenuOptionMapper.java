package com.demo.bbq.business.menuoption.application.mapper;

import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menuoption.domain.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.business.menuoption.domain.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.business.menuoption.domain.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.bbq.business.menuoption.domain.repository.menuoption.entity.MenuOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface MenuOptionMapper {

  MenuOptionResponseDTO toResponseDTO(MenuOptionEntity menuOption);
  MenuOptionEntity toEntity(MenuOptionSaveRequestDTO menuOption);

  MenuOptionEntity toEntity(MenuOptionUpdateRequestDTO menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuOptionResponseDTO toResponseDTO(MenuOptionEntity menuOption, ProductResponseWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuOptionSaveRequestDTO menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuOptionUpdateRequestDTO menuOption, String scope);
}
