package com.demo.bbq.entrypoint.table.registration.rest;

import com.demo.bbq.commons.validations.body.BodyValidator;
import com.demo.bbq.commons.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.validations.headers.HeaderValidator;
import com.demo.bbq.commons.validations.params.ParamValidator;
import com.demo.bbq.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.bbq.entrypoint.table.registration.service.TableRegistrationService;
import com.demo.bbq.commons.restserver.ServerResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class TableRegistrationHandler {

  private final TableRegistrationService tableRegistrationService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    headerValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);

    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(tableRegistrationService::save)
        .flatMap(response -> ServerResponseFactory.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
