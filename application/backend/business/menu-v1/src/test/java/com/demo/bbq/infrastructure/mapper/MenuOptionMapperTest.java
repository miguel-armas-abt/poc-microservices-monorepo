package com.demo.bbq.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.application.mapper.MenuOptionMapper;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.demo.bbq.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.repository.product.wrapper.response.ProductResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuOptionMapperTest {

    private final MenuOptionMapper mapper = Mappers.getMapper(MenuOptionMapper.class);

    private MenuOptionEntity menuOptionEntity;
    private ProductResponseWrapper productWrapper;

    private MenuOptionResponseDTO menuOption;

    private MenuOptionSaveRequestDTO menuOptionSaveRequest;

  @BeforeEach
    public void setup() {
        JsonSerializer jsonSerializer = new JsonSerializer(new ObjectMapper());
        menuOptionEntity = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class).get(0);
        productWrapper = jsonSerializer.readListFromFile("data/product/ProductDto_Array.json", ProductResponseWrapper[].class).get(0);
        menuOption = jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuOptionResponseDTO[].class).get(0);
        menuOptionSaveRequest = jsonSerializer.readElementFromFile("data/menuoption/MenuOptionSaveRequest.json", MenuOptionSaveRequestDTO.class);
    }

    @Test
    public void givenAnEntity_WhenMappingAttributes_ThenObtainResponse() {
        String expected = new Gson().toJson(menuOption);
        String actual = new Gson().toJson(mapper.toResponseDTO(menuOptionEntity, productWrapper));

        assertEquals(expected, actual);
    }

    @Test
    public void givenASaveRequest_WhenMappingAttributes_ThenObtainEntity() {
        menuOptionEntity.setId(null);

        String expected = new Gson().toJson(menuOptionEntity);
        String actual = new Gson().toJson(mapper.toEntity(menuOptionSaveRequest));

        assertEquals(expected, actual);
    }

}
