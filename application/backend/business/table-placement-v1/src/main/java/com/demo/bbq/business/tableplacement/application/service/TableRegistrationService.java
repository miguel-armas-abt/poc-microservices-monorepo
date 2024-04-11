package com.demo.bbq.business.tableplacement.application.service;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponseDTO;
import reactor.core.publisher.Mono;

public interface TableRegistrationService {

  Mono<TableRegistrationResponseDTO> save(TableRegistrationRequestDTO tableRegistrationRequest);

}
