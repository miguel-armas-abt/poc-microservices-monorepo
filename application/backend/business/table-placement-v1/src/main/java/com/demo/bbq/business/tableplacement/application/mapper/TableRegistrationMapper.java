package com.demo.bbq.business.tableplacement.application.mapper;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequestDTO;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponseDTO;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;
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
