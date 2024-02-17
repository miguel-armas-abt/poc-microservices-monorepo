package com.demo.bbq.business.tableplacement.application.service.impl;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponse;
import com.demo.bbq.business.tableplacement.application.mapper.TableRegistrationMapper;
import com.demo.bbq.business.tableplacement.application.service.TableRegistrationService;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.TableOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TableRegistrationServiceImpl implements TableRegistrationService {

  private final TableOrderRepository tableOrderRepository;
  private final TableRegistrationMapper tableRegistrationMapper;

  @Override
  public Mono<TableRegistrationResponse> save(TableRegistrationRequest tableRegistrationRequest) {
    return tableOrderRepository.save(tableRegistrationMapper.fromRequestToDocument(tableRegistrationRequest))
        .map(tableRegistrationMapper::fromDocumentToResponse);
  }

}
