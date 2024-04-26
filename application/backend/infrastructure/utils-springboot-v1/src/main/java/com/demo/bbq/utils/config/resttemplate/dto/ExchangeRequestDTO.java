package com.demo.bbq.utils.config.resttemplate.dto;

import java.io.Serializable;
import java.util.Map;

import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Builder
public class ExchangeRequestDTO<T> implements Serializable {

  private String url;

  private HttpMethod httpMethod;

  private HttpEntity<T> httpEntity;

  private Map<String, String> uriVariables;

  private Class<?> responseClass;

  private Class<? extends ExternalClientErrorWrapper> errorWrapperClass;
}
