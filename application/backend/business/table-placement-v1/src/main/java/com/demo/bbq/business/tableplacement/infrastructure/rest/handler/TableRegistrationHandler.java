package com.demo.bbq.business.tableplacement.infrastructure.rest.handler;

import com.demo.bbq.business.tableplacement.application.service.TableRegistrationService;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponse;
import com.demo.bbq.business.tableplacement.infrastructure.rest.common.BuilderServerResponse;
import com.demo.bbq.business.tableplacement.infrastructure.rest.common.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TableRegistrationHandler {

  private final TableRegistrationService tableRegistrationService;
  private final BuilderServerResponse<TableRegistrationResponse> buildTableOrderResponse;
  private final RequestValidator<TableRegistrationRequest> requestValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(TableRegistrationRequest.class)
        .doOnSuccess(request -> requestValidator.validateRequest(request, TableRegistrationRequest.class))
        .flatMap(tableRegistrationService::save)
        .flatMap(buildTableOrderResponse::build);
  }
}
