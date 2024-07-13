package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.calculator.request.ProductRequestDTO;
import com.demo.bbq.application.dto.calculator.response.InvoiceResponseDTO;
import com.demo.bbq.application.dto.calculator.response.ProductDTO;
import com.demo.bbq.application.dto.rules.DiscountRule;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.application.properties.ApplicationProperties;
import com.demo.bbq.application.service.CalculatorService;
import com.demo.bbq.repository.product.ProductRepository;
import com.demo.bbq.application.rules.service.RuleService;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {

  private final ApplicationProperties properties;
  private final InvoiceMapper invoiceMapper;
  private final ProductRepository productRepository;
  private final RuleService ruleService;

  @Override
  public Mono<InvoiceResponseDTO> calculateInvoice(ServerRequest serverRequest, Flux<ProductRequestDTO> products) {
    InvoiceResponseDTO baseResponse = InvoiceResponseDTO.builder()
        .subtotal(BigDecimal.ZERO)
        .productList(new ArrayList<>())
        .build();

    return products
        .flatMap(product -> productRepository
            .findByProductCode(serverRequest, product.getProductCode())
            .map(productFound -> Pair.of(product, productFound.getUnitPrice())))
        .reduce(baseResponse, (invoice, productWithPrice) -> {
          ProductDTO product = invoiceMapper.toResponseDTO(productWithPrice.getKey(), productWithPrice.getValue(), applyDiscount(productWithPrice.getKey()));
          invoice.getProductList().add(product);
          invoice.setSubtotal(invoice.getSubtotal().add(product.getSubtotal()));
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

  private BigDecimal applyDiscount(ProductRequestDTO product) {
    DiscountRule discountRule = ruleService.process(DiscountRule.builder()
        .quantity(product.getQuantity())
        .productCode(product.getProductCode())
        .build());
    return BigDecimal.valueOf(discountRule.getDiscount());
  }
}
