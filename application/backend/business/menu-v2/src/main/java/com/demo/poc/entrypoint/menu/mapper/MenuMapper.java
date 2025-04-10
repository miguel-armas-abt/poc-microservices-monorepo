package com.demo.poc.entrypoint.menu.mapper;

import com.demo.poc.commons.core.config.MappingConfig;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface MenuMapper {

  MenuResponseDto toResponseDTO(MenuEntity menuOption);

  MenuEntity toEntity(MenuSaveRequestDto menuOption);

  MenuEntity toEntity(MenuUpdateRequestDto menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuResponseDto toResponseDTO(MenuEntity menuOption, ProductResponseWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuSaveRequestDto menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuUpdateRequestDto menuOption, String scope);
}
