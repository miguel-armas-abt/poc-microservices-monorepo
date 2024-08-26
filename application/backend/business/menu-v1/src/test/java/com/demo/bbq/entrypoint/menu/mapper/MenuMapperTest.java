package com.demo.bbq.entrypoint.menu.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.commons.toolkit.serialization.JsonSerializer;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
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
public class MenuMapperTest {

    private final MenuMapper mapper = Mappers.getMapper(MenuMapper.class);

    private MenuEntity menuEntity;
    private ProductResponseWrapper productWrapper;

    private MenuResponseDTO menuOption;

    private MenuSaveRequestDTO menuOptionSaveRequest;

  @BeforeEach
    public void setup() {
        JsonSerializer jsonSerializer = new JsonSerializer(new ObjectMapper());
        menuEntity = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuEntity.class).get(0);
        productWrapper = jsonSerializer.readListFromFile("data/product/ProductDto_Array.json", ProductResponseWrapper.class).get(0);
        menuOption = jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuResponseDTO.class).get(0);
        menuOptionSaveRequest = jsonSerializer.readElementFromFile("data/menuoption/MenuOptionSaveRequest.json", MenuSaveRequestDTO.class);
    }

    @Test
    public void givenAnEntity_WhenMappingAttributes_ThenObtainResponse() {
        String expected = new Gson().toJson(menuOption);
        String actual = new Gson().toJson(mapper.toResponseDTO(menuEntity, productWrapper));

        assertEquals(expected, actual);
    }

    @Test
    public void givenASaveRequest_WhenMappingAttributes_ThenObtainEntity() {
        menuEntity.setId(null);

        String expected = new Gson().toJson(menuEntity);
        String actual = new Gson().toJson(mapper.toEntity(menuOptionSaveRequest));

        assertEquals(expected, actual);
    }

}
