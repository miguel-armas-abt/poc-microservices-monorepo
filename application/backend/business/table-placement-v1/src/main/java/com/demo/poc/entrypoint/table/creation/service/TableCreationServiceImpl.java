package com.demo.poc.entrypoint.table.creation.service;

import com.demo.poc.entrypoint.table.creation.dto.request.TableCreationRequestDto;
import com.demo.poc.entrypoint.table.creation.dto.response.TableCreationResponseDto;
import com.demo.poc.entrypoint.table.creation.mapper.TableCreationMapper;
import com.demo.poc.entrypoint.table.placement.repository.TableOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TableCreationServiceImpl implements TableCreationService {

  private final TableOrderRepository tableOrderRepository;
  private final TableCreationMapper tableCreationMapper;

  @Override
  public Mono<TableCreationResponseDto> save(TableCreationRequestDto tableRegistrationRequest) {
    return tableOrderRepository.save(tableCreationMapper.toDocument(tableRegistrationRequest))
        .map(tableCreationMapper::toResponseDTO);
  }

}
