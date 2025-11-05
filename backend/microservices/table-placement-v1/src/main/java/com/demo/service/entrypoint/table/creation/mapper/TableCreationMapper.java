package com.demo.service.entrypoint.table.creation.mapper;

import com.demo.service.entrypoint.table.creation.dto.request.TableCreationRequestDto;
import com.demo.service.entrypoint.table.creation.dto.response.TableCreationResponseDto;
import com.demo.service.entrypoint.table.placement.repository.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableCreationMapper {

  @Mapping(target = "isAvailable", expression = "java(getDefaultAvailableStatus())")
  @Mapping(target = "menuOrderList", expression = "java(new java.util.ArrayList<>())")
  TableDocument toDocument(TableCreationRequestDto tableRegistrationRequest);

  TableCreationResponseDto toResponseDTO(TableDocument tableOrder);

  default Boolean getDefaultAvailableStatus() {
    return false;
  }
}
