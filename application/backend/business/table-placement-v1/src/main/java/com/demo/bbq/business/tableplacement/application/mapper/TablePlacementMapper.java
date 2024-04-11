package com.demo.bbq.business.tableplacement.application.mapper;

import com.demo.bbq.business.tableplacement.application.dto.tableplacement.request.MenuOrderRequestDTO;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.response.TablePlacementResponseDTO;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.MenuOrderDocument;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TablePlacementMapper {

  @Mapping(target = "id", source = "id")
  TablePlacementResponseDTO toResponseDTO(TableDocument diningRoomOrder);

  MenuOrderDocument toDocument(MenuOrderRequestDTO menuOrder);
}
