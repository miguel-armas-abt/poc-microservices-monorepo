package com.demo.service.entrypoint.menu.mapper;

import com.demo.commons.config.mapper.MappingConfig;
import com.demo.service.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.service.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.service.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.service.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.service.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.service.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.service.entrypoint.menu.repository.menu.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public interface MenuMapper {

  MenuEntity toEntity(MenuSaveRequestDto menuOption);

  MenuEntity toEntity(MenuUpdateRequestDto menuOption, String productCode);

  @Mapping(target = "productCode", source = "product.code")
  MenuResponseDto toResponseDto(MenuEntity menuOption, ProductResponseWrapper product);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuSaveRequestDto menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuUpdateRequestDto menuOption, String scope);
}
