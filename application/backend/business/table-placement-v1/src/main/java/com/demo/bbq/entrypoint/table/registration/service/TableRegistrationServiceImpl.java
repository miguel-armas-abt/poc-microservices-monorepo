package com.demo.bbq.entrypoint.table.registration.service;

import com.demo.bbq.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.bbq.entrypoint.table.registration.dto.response.TableRegistrationResponseDTO;
import com.demo.bbq.entrypoint.table.registration.mapper.TableRegistrationMapper;
import com.demo.bbq.entrypoint.table.placement.repository.TableOrderRepository;
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
