package com.demo.bbq.entrypoint.calculator.service;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import com.demo.bbq.entrypoint.calculator.dto.response.ProductDTO;
import com.demo.bbq.entrypoint.calculator.mapper.CalculatorMapper;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.calculator.repository.discount.DiscountRepository;
import com.demo.bbq.entrypoint.calculator.repository.discount.wrapper.DiscountRequestWrapper;
import com.demo.bbq.entrypoint.calculator.repository.product.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {

  private final ApplicationProperties properties;
  private final CalculatorMapper mapper;
  private final ProductRepository productRepository;
  private final DiscountRepository discountRepository;

  @Override
  public Mono<InvoiceResponseDTO> calculateInvoice(Map<String, String> headers, Flux<ProductRequestDTO> products) {
    InvoiceResponseDTO baseResponse = InvoiceResponseDTO.builder()
        .subtotal(BigDecimal.ZERO)
        .productList(new ArrayList<>())
        .build();

    return products
        .flatMap(product -> productRepository.findByProductCode(headers, product.getProductCode())
            .doOnNext(productFound -> product.setUnitPrice(productFound.getUnitPrice()))
            .map(productFound -> product))
        .flatMap(product -> discountRepository.retrieveDiscount(headers, DiscountRequestWrapper.builder().productCode(product.getProductCode()).quantity(product.getQuantity()).build())
            .doOnNext(discount -> product.setDiscount(discount.getDiscount()))
            .map(productFound -> product))
        .reduce(baseResponse, (invoice, product) -> {
          ProductDTO productResponse = mapper.toResponseDTO(product);
          invoice.getProductList().add(productResponse);
          invoice.setSubtotal(invoice.getSubtotal().add(productResponse.getSubtotal()));
          return invoice;
        })
        .map(this::completeFields);
  }

  private InvoiceResponseDTO completeFields(InvoiceResponseDTO invoice) {
    BigDecimal igv = new BigDecimal(properties.getBusinessInfo().getIgv());
    invoice.setIgv(igv);
    invoice.setTotal(invoice.getSubtotal().add(invoice.getSubtotal().multiply(igv)));
    return invoice;
  }
}
