package com.demo.bbq.entrypoint.table.registration.rest;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.bbq.entrypoint.table.registration.service.TableRegistrationService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
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

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(tableRegistrationService::save)
        .flatMap(response -> ServerResponseBuilderUtil.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
