package com.demo.poc.entrypoint.table.creation.rest;

import com.demo.poc.commons.core.restserver.utils.RestServerUtils;
import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.table.creation.dto.request.TableCreationRequestDto;
import com.demo.poc.entrypoint.table.creation.service.TableCreationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TableCreationHandler {

  private final TableCreationService tableCreationService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    Mono<TableCreationRequestDto> tableCreationRequestMono = serverRequest.bodyToMono(TableCreationRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateAndGet(RestServerUtils.extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .zipWith(tableCreationRequestMono)
        .flatMap(tuple -> tableCreationService.save(tuple.getT2()))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
