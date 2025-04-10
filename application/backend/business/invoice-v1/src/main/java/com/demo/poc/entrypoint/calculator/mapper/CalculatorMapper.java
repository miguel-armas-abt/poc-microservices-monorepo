package com.demo.poc.entrypoint.calculator.mapper;

import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.poc.entrypoint.calculator.dto.response.ProductDto;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CalculatorMapper {

  @Mapping(target = "subtotal", source = "product", qualifiedByName = "getSubtotal")
  ProductDto toResponseDTO(ProductRequestDto product);

  @Named("getSubtotal")
  static BigDecimal getSubtotal(ProductRequestDto product) {
    BigDecimal subtotal = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
    BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
    return subtotal.subtract(subtotal.multiply(discount));
  }
}
