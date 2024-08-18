package com.demo.bbq.entrypoint.table.registration.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HeadersFiller.extractHeadersAsMap;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.bbq.entrypoint.table.registration.service.TableRegistrationService;
import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TableRegistrationHandler {

  private final TableRegistrationService tableRegistrationService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    paramValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);

    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(tableRegistrationService::save)
        .flatMap(response -> ServerResponseFactory.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
