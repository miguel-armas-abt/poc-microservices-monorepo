package com.demo.bbq.utils.restclient.resttemplate.dto;

import com.demo.bbq.utils.errors.handler.external.strategy.ExternalErrorWrapper;
import java.io.Serializable;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Builder
public class ExchangeRequestDTO<I,O> implements Serializable {

  private String url;

  private HttpMethod httpMethod;

  private HttpServletRequest httpServletRequest;

  private Map<String, String> uriVariables;

  private I requestBody;

  private Class<O> responseClass;

  private Class<? extends ExternalErrorWrapper> errorWrapperClass;
}
