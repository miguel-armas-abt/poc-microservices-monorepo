package com.demo.poc.entrypoint.table.placement.mapper;

import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.poc.entrypoint.table.placement.dto.response.PlacementResponseDto;
import com.demo.poc.entrypoint.table.placement.repository.document.MenuOrderDocument;
import com.demo.poc.entrypoint.table.placement.repository.document.TableDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementMapper {

  @Mapping(target = "id", source = "id")
  PlacementResponseDto toResponseDTO(TableDocument diningRoomOrder);

  MenuOrderDocument toDocument(MenuOrderDto menuOrder);
}
