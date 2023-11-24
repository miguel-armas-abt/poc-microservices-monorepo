package com.demo.bbq.business.menu.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menu.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.support.util.JsonFileReader;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuOptionServiceTest {

  @InjectMocks
  private MenuOptionServiceImpl menuOptionService;

  @Mock
  private MenuOptionRepository menuOptionRepository;

  @Spy
  private MenuOptionMapper menuOptionMapper = Mappers.getMapper(MenuOptionMapper.class);

  private List<MenuOptionEntity> expectedSavedMenuOptionEntityList;

  private MenuOptionEntity expectedSavedMenuOptionEntity;

  private List<MenuOption> expectedSavedMenuOptionList;

  private MenuOption expectedSavedMenuOption;

  private MenuOptionSaveRequest menuOptionRequest;

  @Before
  public void setup() throws IOException {
    expectedSavedMenuOptionEntityList = JsonFileReader
        .getList("data/model/menuoption/entity/MenuOption_list.json", MenuOptionEntity[].class);

    expectedSavedMenuOptionEntity = JsonFileReader.getAnElement(
            "data/model/menuoption/entity/MenuOption.json", MenuOptionEntity.class);

    expectedSavedMenuOptionList = JsonFileReader
            .getList("data/model/menuoption/dto/response/MenuOptionResponse_list.json", MenuOption[].class);

    expectedSavedMenuOption = JsonFileReader.getAnElement(
            "data/model/menuoption/dto/response/MenuOptionResponse.json", MenuOption.class);

    menuOptionRequest = JsonFileReader.getAnElement(
            "data/model/menuoption/dto/request/MenuOptionRequest.json", MenuOptionSaveRequest.class);
  }

  @Test
  public void findAll() {
    when(menuOptionRepository.findAll()).thenReturn(expectedSavedMenuOptionEntityList);

    String expected = new Gson().toJson(expectedSavedMenuOptionList);
    String actual = new Gson().toJson(menuOptionService.findByCategory(null));
    assertEquals(expected, actual);
  }

  @Test
  public void findById() {
    when(menuOptionRepository.findById(anyLong())).thenReturn(Optional.of(expectedSavedMenuOptionEntity));

    String expected = new Gson().toJson(expectedSavedMenuOptionEntity);
    String actual = new Gson().toJson(menuOptionService.findByProductCode("MENU00001"));

    assertEquals(expected, actual);
  }

  @Test
  public void findByCategory() {
    when(menuOptionRepository.findByCategory("main-dish")).thenReturn(expectedSavedMenuOptionEntityList
            .stream()
            .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
            .collect(Collectors.toList()));

    String expected = new Gson().toJson(expectedSavedMenuOptionList
            .stream()
            .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
            .collect(Collectors.toList()));
    String actual = new Gson().toJson(menuOptionService.findByCategory("main-dish"));
    assertEquals(expected, actual);
  }

  @Test
  public void save() {
    when(menuOptionRepository.save(any())).thenReturn(expectedSavedMenuOptionEntity);

    String expected = new Gson().toJson(expectedSavedMenuOptionEntity.getId());
//    String actual = new Gson().toJson(menuOptionService.save(menuOptionRequest));
//
//    assertEquals(expected, actual);
  }

  @Test
  public void delete() {
    when(menuOptionRepository.findById(anyLong())).thenReturn(Optional.of(expectedSavedMenuOptionEntity));
    menuOptionService.deleteByProductCode("MENU00001");
  }

}
