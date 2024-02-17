package com.demo.bbq.business.tableplacement.application.service;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponse;
import reactor.core.publisher.Mono;

public interface TableRegistrationService {

  Mono<TableRegistrationResponse> save(TableRegistrationRequest tableRegistrationRequest);

}
