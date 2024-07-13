package com.demo.bbq.entrypoint.calculator.mapper;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.dto.response.ProductDTO;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalculatorMapper {

  @Mapping(target = "subtotal", expression = "java(getSubtotal(product, unitPrice, discount))")
  ProductDTO toResponseDTO(ProductRequestDTO product, BigDecimal unitPrice, BigDecimal discount);

  default BigDecimal getSubtotal(ProductRequestDTO request, BigDecimal unitPrice, BigDecimal discount) {
    BigDecimal subtotal = unitPrice.multiply(new BigDecimal(request.getQuantity()));
    return subtotal.subtract(subtotal.multiply(discount));
  }
}
