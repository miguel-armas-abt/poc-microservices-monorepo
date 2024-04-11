package com.demo.bbq.business.tableplacement.application.mapper;

import com.demo.bbq.business.tableplacement.application.dto.tableregistration.request.TableRegistrationRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableregistration.response.TableRegistrationResponse;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableRegistrationMapper {

  @Mapping(target = "isAvailable", expression = "java(getDefaultAvailableStatus())")
  @Mapping(target = "menuOrderList", expression = "java(new java.util.ArrayList<>())")
  TableDocument fromRequestToDocument(TableRegistrationRequest tableRegistrationRequest);

  TableRegistrationResponse fromDocumentToResponse(TableDocument tableOrder);

  default Boolean getDefaultAvailableStatus() {
    return false;
  }
}
