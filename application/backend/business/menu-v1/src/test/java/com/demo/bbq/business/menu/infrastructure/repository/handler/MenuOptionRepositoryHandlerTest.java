package com.demo.bbq.business.menu.infrastructure.repository.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menu.domain.repository.MenuOptionRepositoryHandler;
import com.demo.bbq.business.menu.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.business.menu.domain.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.product.ProductRepository;
import com.demo.bbq.business.menu.domain.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.utils.files.JsonFileReaderUtil;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuOptionRepositoryHandlerTest {

  @InjectMocks
  private MenuOptionRepositoryHandler menuOptionRepositoryHandler;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private MenuOptionRepository menuOptionRepository;

  @Spy
  private MenuOptionMapper menuOptionMapper = Mappers.getMapper(MenuOptionMapper.class);

  @Before
  public void setup() {

  }

  @Test
  public void givenTwoSourcesInfo_WhenSearchAllMenuOptions_ThenMapResponse() {
    when(productRepository.findByScope(anyString()))
        .thenReturn(JsonFileReaderUtil.getList("data/product/ProductDto_Array.json", ProductResponseWrapper[].class));

    when(menuOptionRepository.findAll())
        .thenReturn(JsonFileReaderUtil.getList("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class));

    String expected = new Gson().toJson(JsonFileReaderUtil.getList("data/menuoption/MenuOption_Array.json", MenuOptionResponseDTO[].class));
    String actual = new Gson().toJson(menuOptionRepositoryHandler.findAll());
    assertEquals(expected, actual);
  }

}