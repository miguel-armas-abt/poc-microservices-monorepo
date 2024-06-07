package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProductResponseDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import com.demo.bbq.application.dto.rules.DiscountRule;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.application.service.ProformaInvoiceService;
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
public class ProformaInvoiceServiceImpl implements ProformaInvoiceService {

  private final ServiceConfigurationProperties properties;
  private final InvoiceMapper proformaInvoiceMapper;
  private final ProductRepository productRepository;
  private final RuleService ruleService;

  @Override
  public Mono<ProformaInvoiceResponseDTO> generateProforma(ServerRequest serverRequest, Flux<ProductRequestDTO> products) {
    ProformaInvoiceResponseDTO initialProforma = ProformaInvoiceResponseDTO.builder()
        .subtotal(BigDecimal.ZERO)
        .productList(new ArrayList<>())
        .build();

    return products
        .flatMap(product -> productRepository
            .findByProductCode(serverRequest, product.getProductCode())
            .map(productFound -> Pair.of(product, productFound.getUnitPrice())))
        .reduce(initialProforma, (proforma, productWithPrice) -> {
          ProductResponseDTO product = proformaInvoiceMapper.toResponseDTO(productWithPrice.getKey(), productWithPrice.getValue(), applyDiscount(productWithPrice.getKey()));
          proforma.getProductList().add(product);
          proforma.setSubtotal(proforma.getSubtotal().add(product.getSubtotal()));
          return proforma;
        })
        .map(this::completeFields);
  }

  private ProformaInvoiceResponseDTO completeFields(ProformaInvoiceResponseDTO proforma) {
    BigDecimal igv = new BigDecimal(properties.getBusinessInfo().getIgv());
    proforma.setIgv(igv);
    proforma.setTotal(proforma.getSubtotal().add(proforma.getSubtotal().multiply(igv)));
    return proforma;
  }

  private BigDecimal applyDiscount(ProductRequestDTO request) {
    DiscountRule discountRule = ruleService.process(DiscountRule.builder()
        .quantity(request.getQuantity())
        .productCode(request.getProductCode())
        .build());
    return BigDecimal.valueOf(discountRule.getDiscount());
  }
}
