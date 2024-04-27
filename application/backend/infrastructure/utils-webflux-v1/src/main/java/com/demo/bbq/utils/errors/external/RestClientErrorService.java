package com.demo.bbq.utils.errors.external;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface RestClientErrorService<T extends ExternalClientErrorWrapper> extends ExternalClientErrorService<T> {

  Mono<Pair<String, String>> getCodeAndMessage(ClientResponse clientResponse);

}
