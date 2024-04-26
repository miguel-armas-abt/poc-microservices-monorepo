package com.demo.bbq.business.menu.domain.repository.product;

import com.demo.bbq.business.menu.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.business.menu.infrastructure.config.resttemplate.CustomRestTemplate;
import com.demo.bbq.utils.config.resttemplate.dto.ExchangeRequestDTO;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

  private static final String SERVICE_NAME = "product-v1";

  private final CustomRestTemplate restTemplate;
  private final ServiceConfigurationProperties properties;

  public ProductResponseWrapper findByCode(String code) {
    return restTemplate.exchange(
        ExchangeRequestDTO.<Void>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.GET)
            .uriVariables(Collections.singletonMap("code", code))
            .responseClass(ProductResponseWrapper.class)
            .errorWrapperClass(ErrorDTO.class)
            .build(), SERVICE_NAME);
  }

  public List<ProductResponseWrapper> findByScope(String scope) {
    return Arrays.asList(restTemplate.exchange(
        ExchangeRequestDTO.<Void>builder()
            .url(getEndpoint().concat("/products?scope={scope}"))
            .httpMethod(HttpMethod.GET)
            .uriVariables(Collections.singletonMap("scope", scope))
            .responseClass(ProductResponseWrapper[].class)
            .errorWrapperClass(ErrorDTO.class)
            .build(), SERVICE_NAME));
  }

  public void save(ProductSaveRequestWrapper productRequest) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ProductSaveRequestWrapper> requestEntity = new HttpEntity<>(productRequest, headers);

    restTemplate.exchange(
        ExchangeRequestDTO.<ProductSaveRequestWrapper>builder()
            .url(getEndpoint().concat("/products"))
            .httpMethod(HttpMethod.POST)
            .httpEntity(requestEntity)
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .build(), SERVICE_NAME);
  }

  public void update(String code, ProductUpdateRequestWrapper productRequest) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ProductUpdateRequestWrapper> requestEntity = new HttpEntity<>(productRequest, headers);

    restTemplate.exchange(
        ExchangeRequestDTO.<ProductUpdateRequestWrapper>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.POST)
            .uriVariables(Collections.singletonMap("code", code))
            .httpEntity(requestEntity)
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .build(), SERVICE_NAME);
  }

  public void delete(String code) {
    restTemplate.exchange(
        ExchangeRequestDTO.<Void>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.DELETE)
            .uriVariables(Collections.singletonMap("code", code))
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .build(), SERVICE_NAME);
  }

  private String getEndpoint() {
    return properties.getRestClients().get(SERVICE_NAME).getRequest().getEndpoint();
  }

}
