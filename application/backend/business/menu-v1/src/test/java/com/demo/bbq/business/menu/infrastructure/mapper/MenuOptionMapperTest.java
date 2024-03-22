package com.demo.bbq.business.menu.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponse;
import com.demo.bbq.business.menu.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.restclient.product.wrapper.response.ProductResponseWrapper;
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
    private ProductResponseWrapper productWrapper;

    private MenuOptionResponse menuOption;

    private MenuOptionSaveRequest menuOptionSaveRequest;

    @BeforeEach
    public void setup() {

        menuOptionEntity = JsonFileReader.getList("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class).get(0);
        productWrapper = JsonFileReader.getList("data/product/ProductDto_Array.json", ProductResponseWrapper[].class).get(0);
        menuOption = JsonFileReader.getList("data/menuoption/MenuOption_Array.json", MenuOptionResponse[].class).get(0);
        menuOptionSaveRequest = JsonFileReader.getAnElement("data/menuoption/MenuOptionSaveRequest.json", MenuOptionSaveRequest.class);
    }

    @Test
    public void givenAnEntity_WhenMappingAttributes_ThenObtainResponse() {
        String expected = new Gson().toJson(menuOption);
        String actual = new Gson().toJson(mapper.fromProductToResponse(menuOptionEntity, productWrapper));

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
