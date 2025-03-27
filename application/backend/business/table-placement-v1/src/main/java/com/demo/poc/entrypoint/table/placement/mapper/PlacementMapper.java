package com.demo.poc.entrypoint.table.placement.mapper;

import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.poc.entrypoint.table.placement.dto.response.PlacementResponseDTO;
import com.demo.poc.entrypoint.table.placement.repository.document.MenuOrderDocument;
import com.demo.poc.entrypoint.table.placement.repository.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementMapper {

  @Mapping(target = "id", source = "id")
  PlacementResponseDTO toResponseDTO(TableDocument diningRoomOrder);

  MenuOrderDocument toDocument(MenuOrderDTO menuOrder);
}
