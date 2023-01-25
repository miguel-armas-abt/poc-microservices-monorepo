package com.demo.bbq.business.menuoption.infrastructure.resource.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.support.util.JsonFileReader;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MenuOptionApi.class)
@ActiveProfiles("test") //use application-test.yaml
public class MenuOptionEntityApiTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MenuOptionService menuOptionService;

  private String URI;
  private List<MenuOption> expectedSavedMenuOptionList;

  @Before
  public void setup() throws IOException {
    expectedSavedMenuOptionList = JsonFileReader
        .getList("data/model/menuoption/dto/response/MenuOptionResponse_list.json", MenuOption[].class);

    URI = "/bbq/business/v1/menu-options";
  }

  @Test
  public void findAll() throws Exception {
    when(menuOptionService.findByCategory(null)).thenReturn(expectedSavedMenuOptionList);
    String expected = new Gson().toJson(expectedSavedMenuOptionList);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI)
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void findByCategory() throws Exception {
    List<MenuOption> expectedFilteredMenuOptionList = expectedSavedMenuOptionList
        .stream()
        .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
        .collect(Collectors.toList());
    when(menuOptionService.findByCategory("main-dish")).thenReturn(expectedFilteredMenuOptionList);
    String expected = new Gson().toJson(expectedFilteredMenuOptionList);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI)
        .queryParam("category", "main-dish")
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void findById() throws Exception {
    MenuOption expectedMenuOption = JsonFileReader
        .getAnElement("data/model/menuoption/dto/response/MenuOptionResponse.json", MenuOption.class);
    when(menuOptionService.findById(anyLong())).thenReturn(expectedMenuOption);
    String expected = new Gson().toJson(expectedMenuOption);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get(URI.concat("/7"))
        .accept(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    String actual = response.getContentAsString(UTF_8);

    assertEquals(expected, actual);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void save() throws Exception {
    MenuOptionRequest requestBody = JsonFileReader
        .getAnElement("data/model/menuoption/dto/request/MenuOptionRequest.json", MenuOptionRequest.class);
    String jsonRequestBody = new Gson().toJson(requestBody);
    when(menuOptionService.save(any(MenuOptionRequest.class))).thenReturn(7L);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(URI)
        .accept(APPLICATION_JSON)
        .content(jsonRequestBody)
        .contentType(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("http://localhost/bbq/business/v1/menu-options/7", response.getHeader("Location"));
  }

  @Test
  public void update() throws Exception {
    MenuOptionRequest requestBody = JsonFileReader
        .getAnElement("data/model/menuoption/dto/request/MenuOptionRequest.json", MenuOptionRequest.class);
    String jsonRequestBody = new Gson().toJson(requestBody);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put(URI.concat("/7"))
        .accept(APPLICATION_JSON)
        .content(jsonRequestBody)
        .contentType(APPLICATION_JSON);

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals("http://localhost/bbq/business/v1/menu-options/7", response.getHeader("Location"));
  }

  @Test
  public void delete() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete(URI.concat("/7"));

    MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }
}