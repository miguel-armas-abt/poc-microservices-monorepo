package com.demo.poc.entrypoint.menu.mapper;

import com.demo.poc.commons.core.serialization.JsonSerializer;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuResponseMapperTest {

  private final MenuResponseMapper mapper = Mappers.getMapper(MenuResponseMapper.class);

  private MenuEntity menuEntity;
  private ProductResponseWrapper productWrapper;

  private MenuResponseDto menuOption;


  @BeforeEach
  public void setup() {
    JsonSerializer jsonSerializer = new JsonSerializer(new ObjectMapper());
    menuEntity = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuEntity.class).get(0);
    productWrapper = jsonSerializer.readListFromFile("data/product/ProductDto_Array.json", ProductResponseWrapper.class).get(0);
    menuOption = jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuResponseDto.class).get(0);
  }

  @Test
  @DisplayName("Given an entity, when mapping attributes, then returns correct response")
  public void givenAnEntity_WhenMappingAttributes_ThenReturnsCorrectResponse() {
    //Arrange
    String expected = new Gson().toJson(menuOption);

    //Act
    String actual = new Gson().toJson(mapper.toResponseDTO(menuEntity, productWrapper));

    //Assert
    assertEquals(expected, actual);
  }

}
