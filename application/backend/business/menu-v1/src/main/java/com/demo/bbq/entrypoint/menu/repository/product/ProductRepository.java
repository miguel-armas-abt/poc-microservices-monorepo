package com.demo.bbq.entrypoint.menu.repository.product;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import com.demo.bbq.commons.core.restclient.CustomRestTemplate;
import com.demo.bbq.commons.core.restclient.dto.ExchangeRequestDTO;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

  private static final String SERVICE_NAME = "product-v1";

  private final CustomRestTemplate restTemplate;
  private final ApplicationProperties properties;

  public ProductResponseWrapper findByCode(Map<String, String> headers, String code) {
    return restTemplate.exchange(
        ExchangeRequestDTO.<Void, ProductResponseWrapper>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.GET)
            .uriVariables(Collections.singletonMap("code", code))
            .responseClass(ProductResponseWrapper.class)
            .errorWrapperClass(ErrorDTO.class)
            .headers(headers)
            .build(), SERVICE_NAME);
  }

  public List<ProductResponseWrapper> findByScope(Map<String, String> headers, String scope) {
    return Arrays.asList(restTemplate.exchange(
        ExchangeRequestDTO.<Void, ProductResponseWrapper[]>builder()
            .url(getEndpoint().concat("/products?scope={scope}"))
            .httpMethod(HttpMethod.GET)
            .uriVariables(Collections.singletonMap("scope", scope))
            .responseClass(ProductResponseWrapper[].class)
            .errorWrapperClass(ErrorDTO.class)
            .headers(headers)
            .build(), SERVICE_NAME));
  }

  public void save(Map<String, String> headers, ProductSaveRequestWrapper productRequest) {
    restTemplate.exchange(
        ExchangeRequestDTO.<ProductSaveRequestWrapper, Void>builder()
            .url(getEndpoint().concat("/products"))
            .httpMethod(HttpMethod.POST)
            .requestBody(productRequest)
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .headers(headers)
            .build(), SERVICE_NAME);
  }

  public void update(Map<String, String> headers, String code, ProductUpdateRequestWrapper productRequest) {
    restTemplate.exchange(
        ExchangeRequestDTO.<ProductUpdateRequestWrapper, Void>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.PUT)
            .uriVariables(Collections.singletonMap("code", code))
            .requestBody(productRequest)
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .headers(headers)
            .build(), SERVICE_NAME);
  }

  public void delete(Map<String, String> headers, String code) {
    restTemplate.exchange(
        ExchangeRequestDTO.<Void, Void>builder()
            .url(getEndpoint().concat("/products/{code}"))
            .httpMethod(HttpMethod.DELETE)
            .uriVariables(Collections.singletonMap("code", code))
            .responseClass(Void.class)
            .errorWrapperClass(ErrorDTO.class)
            .headers(headers)
            .build(), SERVICE_NAME);
  }

  private String getEndpoint() {
    return properties.getRestClients().get(SERVICE_NAME).getRequest().getEndpoint();
  }

}
