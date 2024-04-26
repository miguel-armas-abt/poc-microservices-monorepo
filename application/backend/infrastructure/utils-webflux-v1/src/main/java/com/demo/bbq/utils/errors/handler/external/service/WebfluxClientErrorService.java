package com.demo.bbq.utils.errors.handler.external.service;

import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorService;
import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface WebfluxClientErrorService<T extends ExternalClientErrorWrapper> extends ExternalClientErrorService<T> {

  Mono<Pair<String, String>> getCodeAndMessage(ClientResponse clientResponse);

}
