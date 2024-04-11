package com.demo.bbq.business.tableplacement.infrastructure.rest.handler;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponseDTO;
import com.demo.bbq.business.tableplacement.application.service.TableRegistrationService;
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
  private final BuilderServerResponse<TableRegistrationResponseDTO> buildTableOrderResponse;
  private final RequestValidator<TableRegistrationRequestDTO> requestValidator;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnSuccess(request -> requestValidator.validateRequest(request, TableRegistrationRequestDTO.class))
        .flatMap(tableRegistrationService::save)
        .flatMap(buildTableOrderResponse::build);
  }
}
