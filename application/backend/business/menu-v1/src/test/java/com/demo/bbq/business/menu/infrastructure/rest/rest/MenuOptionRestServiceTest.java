package com.demo.bbq.business.menu.infrastructure.rest.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponse;
import com.demo.bbq.business.menu.application.service.MenuOptionService;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.infrastructure.rest.MenuOptionRestServiceImpl;
import com.demo.bbq.support.util.JsonFileReader;
import com.google.gson.Gson;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MenuOptionRestServiceImpl.class)
public class MenuOptionRestServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MenuOptionService menuOptionService;

  private String URI;
  private List<MenuOptionResponse> expectedSavedMenuOptionList;

  @Before
  public void setup() {
    expectedSavedMenuOptionList = JsonFileReader.getList("data/menuoption/MenuOption_Array.json", MenuOptionResponse[].class);
    URI = "/bbq/business/menu/v1/menu-options";
  }

  @Test
  public void givenCategoryNull_WhenSearchMenuOptions_ThenReturnAllItems() throws Exception {
    when(menuOptionService.findByCategory(any())).thenReturn(expectedSavedMenuOptionList);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI)
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    String expected = new Gson().toJson(expectedSavedMenuOptionList);
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void givenCategory_WhenSearchMenuOptions_ThenReturnFilteredItems() throws Exception {
    when(menuOptionService.findByCategory(anyString())).thenReturn(expectedSavedMenuOptionList);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI)
        .queryParam("category", "MAIN")
        .accept(APPLICATION_JSON);
    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    String expected = new Gson().toJson(expectedSavedMenuOptionList);
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void givenProductCode_WhenSearchMenuOptionByProductCode_ThenReturnSearchedMenuOption() throws Exception {
    MenuOptionResponse expectedMenuOption = JsonFileReader.getList("data/menuoption/MenuOption_Array.json", MenuOptionResponse[].class).get(0);
    when(menuOptionService.findByProductCode(anyString())).thenReturn(expectedMenuOption);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI.concat("/MENU0001"))
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    String expected = new Gson().toJson(expectedMenuOption);
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void givenSaveRequest_WhenSaveMenuOption_ThenResponseShowTheAffectedResource() throws Exception {
    MenuOptionSaveRequest saveRequest = JsonFileReader.getAnElement("data/menuoption/MenuOptionSaveRequest.json", MenuOptionSaveRequest.class);
    doNothing().when(menuOptionService).save(any(MenuOptionSaveRequest.class));

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(URI)
        .accept(APPLICATION_JSON)
        .content(new Gson().toJson(saveRequest))
        .contentType(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("http://localhost".concat(URI).concat("/MENU0001"), response.getHeader("Location"));
  }

  @Test
  public void givenDeleteRequest_WhenDeleteMenuOption_ThenResponseShowTheAffectedResource() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete(URI.concat("/MENU0001"));

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }
}