package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.application.service.TableRegistrationService;
import com.demo.bbq.utils.toolkit.RequestValidator;
import com.demo.bbq.utils.toolkit.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TableRegistrationHandler {

  private final TableRegistrationService tableRegistrationService;
  private final RequestValidator requestValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnSuccess(requestValidator::validateRequestBody)
        .flatMap(tableRegistrationService::save)
        .flatMap(response -> ServerResponseBuilderUtil.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
