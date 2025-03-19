package com.demo.bbq.entrypoint.menu.mapper;

import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface MenuResponseMapper {

  @Mapping(target = "productCode", source = "product.code")
  MenuResponseDTO toResponseDTO(MenuEntity menuOption, ProductResponseWrapper product);

  default List<MenuResponseDTO> toResponseDTO(Map<MenuEntity, ProductResponseWrapper> menuAndProductMap) {
    return menuAndProductMap.entrySet().stream()
        .map(menuAndProduct -> this.toResponseDTO(menuAndProduct.getKey(), menuAndProduct.getValue()))
        .toList();
  }
}
