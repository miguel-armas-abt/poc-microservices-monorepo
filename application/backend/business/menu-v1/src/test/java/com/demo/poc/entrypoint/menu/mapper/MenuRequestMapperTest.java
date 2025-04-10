package com.demo.poc.entrypoint.menu.mapper;

import com.demo.poc.commons.core.serialization.JsonSerializer;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuRequestMapperTest {

  private final MenuRequestMapper mapper = Mappers.getMapper(MenuRequestMapper.class);

  private MenuEntity menuEntity;

  private MenuSaveRequestDto menuOptionSaveRequest;

  @BeforeEach
  public void setup() {
    JsonSerializer jsonSerializer = new JsonSerializer(new ObjectMapper());
    menuEntity = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuEntity.class).get(0);
    menuOptionSaveRequest = jsonSerializer.readElementFromFile("data/menuoption/MenuOptionSaveRequest.json", MenuSaveRequestDto.class);
  }

  @Test
  public void givenASaveRequest_WhenMappingAttributes_ThenObtainEntity() {
    //Arrange
    menuEntity.setId(null);
    String expected = new Gson().toJson(menuEntity);

    //Act
    String actual = new Gson().toJson(mapper.toEntity(menuOptionSaveRequest));

    //Assert
    assertEquals(expected, actual);
  }
}