package com.demo.service.entrypoint.table.creation.service;

import com.demo.service.entrypoint.table.creation.dto.request.TableCreationRequestDto;
import com.demo.service.entrypoint.table.creation.dto.response.TableCreationResponseDto;
import reactor.core.publisher.Mono;

public interface TableCreationService {

  Mono<TableCreationResponseDto> save(TableCreationRequestDto tableRegistrationRequest);

}
