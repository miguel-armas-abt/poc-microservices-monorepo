package com.demo.bbq.entrypoint.calculator.mapper;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.dto.response.ProductDTO;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CalculatorMapper {

  @Mapping(target = "subtotal", source = "product", qualifiedByName = "getSubtotal")
  ProductDTO toResponseDTO(ProductRequestDTO product);

  @Named("getSubtotal")
  static BigDecimal getSubtotal(ProductRequestDTO product) {
    BigDecimal subtotal = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
    BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
    return subtotal.subtract(subtotal.multiply(discount));
  }
}
