package com.demo.bbq.business.menu.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.business.menu.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.support.util.JsonFileReader;
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
    private ProductDto productDto;

    private MenuOption menuOption;

    private MenuOptionSaveRequest menuOptionSaveRequest;

    @BeforeEach
    public void setup() {

        menuOptionEntity = JsonFileReader.getList("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class).get(0);
        productDto = JsonFileReader.getList("data/product/ProductDto_Array.json", ProductDto[].class).get(0);
        menuOption = JsonFileReader.getList("data/menuoption/MenuOption_Array.json", MenuOption[].class).get(0);
        menuOptionSaveRequest = JsonFileReader.getAnElement("data/menuoption/MenuOptionSaveRequest.json", MenuOptionSaveRequest.class);
    }

    @Test
    public void givenAnEntity_WhenMappingAttributes_ThenObtainResponse() {
        String expected = new Gson().toJson(menuOption);
        String actual = new Gson().toJson(mapper.fromProductToResponse(menuOptionEntity, productDto));

        assertEquals(expected, actual);
    }

    @Test
    public void givenASaveRequest_WhenMappingAttributes_ThenObtainEntity() {
        menuOptionEntity.setId(null);

        String expected = new Gson().toJson(menuOptionEntity);
        String actual = new Gson().toJson(mapper.fromSaveRequestToEntity(menuOptionSaveRequest));

        assertEquals(expected, actual);
    }

}
