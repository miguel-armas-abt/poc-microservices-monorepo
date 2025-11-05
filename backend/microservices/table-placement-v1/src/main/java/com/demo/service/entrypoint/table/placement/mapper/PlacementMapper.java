package com.demo.service.entrypoint.table.placement.mapper;

import com.demo.service.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.service.entrypoint.table.placement.dto.response.PlacementResponseDto;
import com.demo.service.entrypoint.table.placement.repository.document.MenuOrderDocument;
import com.demo.service.entrypoint.table.placement.repository.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementMapper {

  @Mapping(target = "id", source = "id")
  PlacementResponseDto toResponseDTO(TableDocument diningRoomOrder);

  MenuOrderDocument toDocument(MenuOrderDto menuOrder);
}
