package com.demo.bbq.entrypoint.table.registration.service;

import com.demo.bbq.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.bbq.entrypoint.table.registration.dto.response.TableRegistrationResponseDTO;
import reactor.core.publisher.Mono;

public interface TableRegistrationService {

  Mono<TableRegistrationResponseDTO> save(TableRegistrationRequestDTO tableRegistrationRequest);

}
