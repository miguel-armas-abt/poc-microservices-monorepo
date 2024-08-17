package com.demo.bbq.entrypoint.menu.repository;

import static com.demo.bbq.entrypoint.menu.rest.HttpServletRequestBase.buildHttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.demo.bbq.entrypoint.menu.mapper.MenuMapper;
import com.demo.bbq.entrypoint.menu.repository.menu.MenuRepository;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.ProductRepository;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.entrypoint.menu.service.MenuProductMatcher;
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
public class MenuProductMatcherTest {

  @InjectMocks
  private MenuProductMatcher menuProductMatcher;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private MenuRepository menuRepository;

  @Spy
  private MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class);

  private JsonSerializer jsonSerializer;

  @Before
  public void setup() {
    jsonSerializer = new JsonSerializer(new ObjectMapper());
  }

  @Test
  public void givenTwoSourcesInfo_WhenSearchAllMenuOptions_ThenMapResponse() {
    when(productRepository.findByScope(any(), anyString()))
        .thenReturn(jsonSerializer.readListFromFile("data/product/ProductDto_Array.json", ProductResponseWrapper.class));

    when(menuRepository.findAll())
        .thenReturn(jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuEntity.class));

    String expected = new Gson().toJson(jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuResponseDTO.class));
    String actual = new Gson().toJson(menuProductMatcher.findAll(buildHttpServletRequest()));
    assertEquals(expected, actual);
  }

}