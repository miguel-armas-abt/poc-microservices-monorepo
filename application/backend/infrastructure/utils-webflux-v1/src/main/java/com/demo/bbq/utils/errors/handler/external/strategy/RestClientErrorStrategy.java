package com.demo.bbq.utils.errors.handler.external.strategy;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface RestClientErrorStrategy extends ExternalErrorStrategy {

  Mono<Pair<String, String>> getCodeAndMessage(ClientResponse clientResponse);

}
