package com.demo.bbq.business.menu.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menu.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.utils.files.JsonFileReaderUtil;
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

        menuOptionEntity = JsonFileReaderUtil.getList("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class).get(0);
        productWrapper = JsonFileReaderUtil.getList("data/product/ProductDto_Array.json", ProductResponseWrapper[].class).get(0);
        menuOption = JsonFileReaderUtil.getList("data/menuoption/MenuOption_Array.json", MenuOptionResponseDTO[].class).get(0);
        menuOptionSaveRequest = JsonFileReaderUtil.getAnElement("data/menuoption/MenuOptionSaveRequest.json", MenuOptionSaveRequestDTO.class);
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
