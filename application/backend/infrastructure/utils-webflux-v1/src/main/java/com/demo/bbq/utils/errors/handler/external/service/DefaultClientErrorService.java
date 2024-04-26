package com.demo.bbq.utils.errors.handler.external.service;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class DefaultClientErrorService implements WebfluxClientErrorService<ErrorDTO> {

  @Override
  public Mono<Pair<String, String>> getCodeAndMessage(ClientResponse clientResponse) {
    return clientResponse
        .bodyToMono(String.class)
        .map(errorBody -> new Gson().fromJson(errorBody, ErrorDTO.class))
        .map(responseBodyError -> Pair.of(responseBodyError.getCode(), responseBodyError.getMessage()));
  }

  @Override
  public boolean supports(Class<?> wrapperClass) {
    return wrapperClass.isAssignableFrom(ErrorDTO.class);
  }
}
