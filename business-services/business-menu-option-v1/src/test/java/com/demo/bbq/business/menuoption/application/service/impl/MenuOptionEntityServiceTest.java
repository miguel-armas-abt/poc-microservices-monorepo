package com.demo.bbq.business.menuoption.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menuoption.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
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
public class MenuOptionEntityServiceTest {

  @InjectMocks
  private MenuOptionServiceImpl menuOptionService;

  @Mock
  private MenuOptionRepository menuOptionRepository;

  @Spy
  private MenuOptionMapper menuOptionMapper = Mappers.getMapper(MenuOptionMapper.class);

  private List<MenuOptionEntity> expectedSavedMenuOptionEntityListEntity;

  private MenuOptionEntity expectedSavedMenuOptionEntityEntity;

  private List<MenuOption> expectedSavedMenuOptionList;

  private MenuOption expectedSavedMenuOption;

  private MenuOptionRequest menuOptionRequest;

  @Before
  public void setup() throws IOException {
    expectedSavedMenuOptionEntityListEntity = JsonFileReader
        .getList("data/model/menuoption/entity/MenuOption_list.json", MenuOptionEntity[].class);

    expectedSavedMenuOptionEntityEntity = JsonFileReader.getAnElement(
            "data/model/menuoption/entity/MenuOption.json", MenuOptionEntity.class);

    expectedSavedMenuOptionList = JsonFileReader
            .getList("data/model/menuoption/dto/response/MenuOptionResponse_list.json", MenuOption[].class);

    expectedSavedMenuOption = JsonFileReader.getAnElement(
            "data/model/menuoption/dto/response/MenuOptionResponse.json", MenuOption.class);

    menuOptionRequest = JsonFileReader.getAnElement(
            "data/model/menuoption/dto/request/MenuOptionRequest.json", MenuOptionRequest.class);
  }

  @Test
  public void findAll() {
    when(menuOptionRepository.findAll()).thenReturn(expectedSavedMenuOptionEntityListEntity);

    String expected = new Gson().toJson(expectedSavedMenuOptionEntityListEntity);
    String actual = new Gson().toJson(menuOptionService.findByCategory(null));
    assertEquals(expected, actual);
  }

  @Test
  public void findById() {
    when(menuOptionRepository.findById(anyLong())).thenReturn(Optional.of(expectedSavedMenuOptionEntityEntity));

    String expected = new Gson().toJson(expectedSavedMenuOptionEntityEntity);
    String actual = new Gson().toJson(menuOptionService.findById(7L));

    assertEquals(expected, actual);
  }

  @Test
  public void findByCategory() {
    when(menuOptionRepository.findByCategory("main-dish")).thenReturn(expectedSavedMenuOptionEntityListEntity
            .stream()
            .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
            .collect(Collectors.toList()));

    String expected = new Gson().toJson(expectedSavedMenuOptionEntityListEntity
            .stream()
            .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
            .collect(Collectors.toList()));
    String actual = new Gson().toJson(menuOptionService.findByCategory("main-dish"));
    assertEquals(expected, actual);
  }

  @Test
  public void save() {
    when(menuOptionRepository.save(any())).thenReturn(expectedSavedMenuOptionEntityEntity);

    String expected = new Gson().toJson(expectedSavedMenuOptionEntityEntity.getId());
    String actual = new Gson().toJson(menuOptionService.save(menuOptionRequest));

    assertEquals(expected, actual);
  }

  @Test
  public void delete() {
    when(menuOptionRepository.findById(anyLong())).thenReturn(Optional.of(expectedSavedMenuOptionEntityEntity));
    menuOptionService.deleteById(7L);
  }

}