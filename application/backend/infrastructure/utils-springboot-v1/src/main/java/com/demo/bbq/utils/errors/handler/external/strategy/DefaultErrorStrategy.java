package com.demo.bbq.utils.errors.handler.external.strategy;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.HttpStatusCodeException;

public class DefaultErrorStrategy implements RestClientErrorStrategy {

  @Override
  public Pair<String, String> getCodeAndMessage(HttpStatusCodeException httpException) {
    ErrorDTO responseBodyError = new Gson().fromJson(httpException.getResponseBodyAsString(), ErrorDTO.class);
    return Pair.of(responseBodyError.getCode(), responseBodyError.getMessage());
  }

  @Override
  public boolean supports(Class<? extends ExternalErrorWrapper> errorWrapperClass) {
    return errorWrapperClass.isAssignableFrom(ErrorDTO.class);
  }
}
