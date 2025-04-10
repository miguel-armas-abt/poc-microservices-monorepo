package com.demo.poc.entrypoint.menu.mapper;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuRequestMapper {

  MenuEntity toEntity(MenuSaveRequestDto menuOption);

  MenuEntity toEntity(MenuUpdateRequestDto menuOption, String productCode);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuSaveRequestDto menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuUpdateRequestDto menuOption, String scope);
}
