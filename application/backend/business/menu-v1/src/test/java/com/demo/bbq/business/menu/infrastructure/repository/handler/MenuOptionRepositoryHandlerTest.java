package com.demo.bbq.business.menu.infrastructure.repository.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.ProductApi;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.support.util.JsonFileReader;
import com.google.gson.Gson;
import io.reactivex.Single;
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
  private ProductApi productApi;

  @Mock
  private MenuOptionRepository menuOptionRepository;

  @Spy
  private MenuOptionMapper menuOptionMapper = Mappers.getMapper(MenuOptionMapper.class);

  @Before
  public void setup() {

  }

  @Test
  public void givenTwoSourcesInfo_WhenSearchAllMenuOptions_ThenMapResponse() {
    when(productApi.findByScope(anyString()))
        .thenReturn(Single.just(JsonFileReader.getList("data/product/ProductDto_Array.json", ProductDto[].class)));

    when(menuOptionRepository.findAll())
        .thenReturn(JsonFileReader.getList("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class));

    String expected = new Gson().toJson(JsonFileReader.getList("data/menuoption/MenuOption_Array.json", MenuOption[].class));
    String actual = new Gson().toJson(menuOptionRepositoryHandler.findAll());
    assertEquals(expected, actual);
  }

}