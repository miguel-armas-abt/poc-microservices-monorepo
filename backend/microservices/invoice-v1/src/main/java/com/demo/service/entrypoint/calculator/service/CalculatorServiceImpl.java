package com.demo.service.entrypoint.calculator.service;

import com.demo.service.entrypoint.calculator.dto.request.ProductRequestDto;
import com.demo.service.entrypoint.calculator.dto.response.InvoiceResponseDto;
import com.demo.service.entrypoint.calculator.dto.response.ProductDto;
import com.demo.service.entrypoint.calculator.mapper.CalculatorMapper;
import com.demo.service.commons.properties.ApplicationProperties;
import com.demo.service.entrypoint.calculator.repository.product.ProductRepository;
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

  @Override
  public Mono<InvoiceResponseDto> calculateInvoice(Map<String, String> headers, Flux<ProductRequestDto> products) {
    InvoiceResponseDto baseResponse = InvoiceResponseDto.builder()
        .subtotal(BigDecimal.ZERO)
        .productList(new ArrayList<>())
        .build();

    return products
        .flatMap(product -> productRepository.findByProductCode(headers, product.getProductCode())
            .doOnNext(productFound -> product.setUnitPrice(productFound.getUnitPrice()))
            .map(productFound -> product))
        .reduce(baseResponse, (invoice, product) -> {
          ProductDto productResponse = mapper.toResponseDto(product);
          invoice.getProductList().add(productResponse);
          invoice.setSubtotal(invoice.getSubtotal().add(productResponse.getSubtotal()));
          return invoice;
        })
        .map(this::completeFields);
  }

  private InvoiceResponseDto completeFields(InvoiceResponseDto invoice) {
    BigDecimal igv = new BigDecimal(properties.getCustom().getFunctional().getIgv());
    invoice.setIgv(igv);
    invoice.setTotal(invoice.getSubtotal().add(invoice.getSubtotal().multiply(igv)));
    return invoice;
  }
}
