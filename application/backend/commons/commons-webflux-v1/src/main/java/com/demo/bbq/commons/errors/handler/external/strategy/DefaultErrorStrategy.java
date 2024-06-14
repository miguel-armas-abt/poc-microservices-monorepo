package com.demo.bbq.commons.errors.handler.external.strategy;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class DefaultErrorStrategy implements RestClientErrorStrategy {

  @Override
  public Mono<Pair<String, String>> getCodeAndMessage(ClientResponse clientResponse) {
    return clientResponse
        .bodyToMono(String.class)
        .map(errorBody -> new Gson().fromJson(errorBody, ErrorDTO.class))
        .map(responseBodyError -> Pair.of(responseBodyError.getCode(), responseBodyError.getMessage()));
  }

  @Override
  public boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass) {
    return wrapperClass.isAssignableFrom(ErrorDTO.class);
  }

}
