package com.demo.service.entrypoint.calculator.mapper;

import com.demo.service.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.service.entrypoint.calculator.dto.response.ProductDto;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CalculatorMapper {

  @Mapping(target = "subtotal", source = "product", qualifiedByName = "getSubtotal")
  ProductDto toResponseDto(ProductRequestDto product);

  @Named("getSubtotal")
  static BigDecimal getSubtotal(ProductRequestDto product) {
    return product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
  }
}
