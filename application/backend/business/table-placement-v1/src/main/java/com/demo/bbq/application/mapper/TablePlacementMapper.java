package com.demo.bbq.application.mapper;

import com.demo.bbq.application.dto.tableplacement.request.MenuOrderRequestDTO;
import com.demo.bbq.application.dto.tableplacement.response.TablePlacementResponseDTO;
import com.demo.bbq.repository.tableorder.document.MenuOrderDocument;
import com.demo.bbq.repository.tableorder.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TablePlacementMapper {

  @Mapping(target = "id", source = "id")
  TablePlacementResponseDTO toResponseDTO(TableDocument diningRoomOrder);

  MenuOrderDocument toDocument(MenuOrderRequestDTO menuOrder);
}
