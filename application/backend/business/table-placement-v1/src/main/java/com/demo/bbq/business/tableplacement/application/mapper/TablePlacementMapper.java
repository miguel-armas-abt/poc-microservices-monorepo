package com.demo.bbq.business.tableplacement.application.mapper;

import com.demo.bbq.business.tableplacement.application.dto.tableplacement.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.response.TablePlacementResponse;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.MenuOrderDocument;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TablePlacementMapper {

  @Mapping(target = "id", source = "id")
  TablePlacementResponse fromDocumentToResponse(TableDocument diningRoomOrder);

  MenuOrderDocument fromRequestToDocument(MenuOrderRequest menuOrder);
}
