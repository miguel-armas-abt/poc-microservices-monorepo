package com.demo.bbq.entrypoint.menu.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import com.demo.bbq.entrypoint.menu.service.MenuService;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
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
@WebMvcTest(value = MenuRestServiceImpl.class)
public class MenuRestServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MenuService menuService;

  private String URI;
  private List<MenuResponseDTO> expectedSavedMenuOptionList;

  private JsonSerializer jsonSerializer;

  @Before
  public void setup() {
    jsonSerializer = new JsonSerializer(new ObjectMapper());
    expectedSavedMenuOptionList = jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuResponseDTO.class);
    URI = "/bbq/business/menu/v1/menu-options";
  }

  @Ignore
  @Test
  public void givenCategoryNull_WhenSearchMenuOptions_ThenReturnAllItems() throws Exception {
    when(menuService.findByCategory(any(), any())).thenReturn(expectedSavedMenuOptionList);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI)
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    String expected = new Gson().toJson(expectedSavedMenuOptionList);
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Ignore
  @Test
  public void givenCategory_WhenSearchMenuOptions_ThenReturnFilteredItems() throws Exception {
    when(menuService.findByCategory(any(), anyString())).thenReturn(expectedSavedMenuOptionList);

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

  @Ignore
  @Test
  public void givenProductCode_WhenSearchMenuOptionByProductCode_ThenReturnSearchedMenuOption() throws Exception {
    MenuResponseDTO expectedMenuOption = jsonSerializer.readListFromFile("data/menuoption/MenuOption_Array.json", MenuResponseDTO.class).get(0);
    when(menuService.findByProductCode(any(), anyString())).thenReturn(expectedMenuOption);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI.concat("/MENU0001"))
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

    String expected = new Gson().toJson(expectedMenuOption);
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Ignore
  @Test
  public void givenSaveRequest_WhenSaveMenuOption_ThenResponseShowTheAffectedResource() throws Exception {
    MenuSaveRequestDTO saveRequest = jsonSerializer.readElementFromFile("data/menuoption/MenuOptionSaveRequest.json", MenuSaveRequestDTO.class);
    doNothing().when(menuService).save(any(), any(MenuSaveRequestDTO.class));

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(URI)
        .accept(APPLICATION_JSON)
        .content(new Gson().toJson(saveRequest))
        .contentType(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("http://localhost".concat(URI).concat("/MENU0001"), response.getHeader("Location"));
  }

  @Ignore
  @Test
  public void givenDeleteRequest_WhenDeleteMenuOption_ThenResponseShowTheAffectedResource() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete(URI.concat("/MENU0001"));

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }
}