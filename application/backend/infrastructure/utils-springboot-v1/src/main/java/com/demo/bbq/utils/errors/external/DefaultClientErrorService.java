package com.demo.bbq.utils.errors.external;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.HttpStatusCodeException;

public class DefaultClientErrorService implements RestClientErrorService<ErrorDTO> {

  @Override
  public Pair<String, String> getCodeAndMessage(HttpStatusCodeException httpException) {
    ErrorDTO responseBodyError = new Gson().fromJson(httpException.getResponseBodyAsString(), ErrorDTO.class);
    return Pair.of(responseBodyError.getCode(), responseBodyError.getMessage());
  }

  @Override
  public boolean supports(Class<?> wrapperClass) {
    return wrapperClass.isAssignableFrom(ErrorDTO.class);
  }
}
