package com.demo.bbq.infrastructure.repository.handler;

import static com.demo.bbq.infrastructure.rest.rest.HttpServletRequestBase.buildHttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.demo.bbq.repository.MenuOptionRepositoryHandler;
import com.demo.bbq.application.mapper.MenuOptionMapper;
import com.demo.bbq.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.repository.product.ProductRepository;
import com.demo.bbq.repository.product.wrapper.response.ProductResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private JsonSerializer jsonSerializer;

  @Before
  public void setup() {
    jsonSerializer = new JsonSerializer(new ObjectMapper());
  }

  @Test
  public void givenTwoSourcesInfo_WhenSearchAllMenuOptions_ThenMapResponse() {
    when(productRepository.findByScope(any(), anyString()))
        .thenReturn(jsonSerializer.readListFromFile("data/product/ProductDto_Array.json", ProductResponseWrapper[].class));

    when(menuOptionRepository.findAll())
        .thenReturn(jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class));

    String expected = new Gson().toJson(jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuOptionResponseDTO[].class));
    String actual = new Gson().toJson(menuOptionRepositoryHandler.findAll(buildHttpServletRequest()));
    assertEquals(expected, actual);
  }

}