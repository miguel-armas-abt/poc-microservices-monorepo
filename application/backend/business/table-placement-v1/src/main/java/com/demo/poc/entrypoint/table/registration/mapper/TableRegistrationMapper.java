package com.demo.poc.entrypoint.table.registration.mapper;

import com.demo.poc.entrypoint.table.registration.dto.request.TableRegistrationRequestDTO;
import com.demo.poc.entrypoint.table.registration.dto.response.TableRegistrationResponseDTO;
import com.demo.poc.entrypoint.table.placement.repository.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableRegistrationMapper {

  @Mapping(target = "isAvailable", expression = "java(getDefaultAvailableStatus())")
  @Mapping(target = "menuOrderList", expression = "java(new java.util.ArrayList<>())")
  TableDocument toDocument(TableRegistrationRequestDTO tableRegistrationRequest);

  TableRegistrationResponseDTO toResponseDTO(TableDocument tableOrder);

  default Boolean getDefaultAvailableStatus() {
    return false;
  }
}
