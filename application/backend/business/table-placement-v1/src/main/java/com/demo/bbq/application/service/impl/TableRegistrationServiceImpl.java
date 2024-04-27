package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.application.dto.tableregistration.response.TableRegistrationResponseDTO;
import com.demo.bbq.application.mapper.TableRegistrationMapper;
import com.demo.bbq.application.service.TableRegistrationService;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TableRegistrationServiceImpl implements TableRegistrationService {

  private final TableOrderRepository tableOrderRepository;
  private final TableRegistrationMapper tableRegistrationMapper;

  @Override
  public Mono<TableRegistrationResponseDTO> save(TableRegistrationRequestDTO tableRegistrationRequest) {
    return tableOrderRepository.save(tableRegistrationMapper.toDocument(tableRegistrationRequest))
        .map(tableRegistrationMapper::toResponseDTO);
  }

}
