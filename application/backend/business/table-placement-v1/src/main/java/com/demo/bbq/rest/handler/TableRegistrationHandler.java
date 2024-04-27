package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.application.dto.tableregistration.response.TableRegistrationResponseDTO;
import com.demo.bbq.application.service.TableRegistrationService;
import com.demo.bbq.rest.common.BuilderServerResponse;
import com.demo.bbq.application.helper.RequestValidatorHelper;
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
  private final RequestValidatorHelper<TableRegistrationRequestDTO> requestValidatorHelper;

  public Mono<ServerResponse> createTable(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(TableRegistrationRequestDTO.class)
        .doOnSuccess(requestValidatorHelper::validateRequestBody)
        .flatMap(tableRegistrationService::save)
        .flatMap(buildTableOrderResponse::build);
  }
}
