package com.demo.poc.entrypoint.menu.mapper;

import com.demo.poc.commons.core.config.MappingConfig;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface MenuMapper {

  MenuResponseDTO toResponseDTO(MenuEntity menuOption);

  MenuEntity toEntity(MenuSaveRequestDTO menuOption);

  MenuEntity toEntity(MenuUpdateRequestDTO menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuResponseDTO toResponseDTO(MenuEntity menuOption, ProductResponseWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuSaveRequestDTO menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuUpdateRequestDTO menuOption, String scope);
}
