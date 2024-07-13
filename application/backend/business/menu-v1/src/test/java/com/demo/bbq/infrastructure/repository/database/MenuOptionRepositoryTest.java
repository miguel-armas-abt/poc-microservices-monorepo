package com.demo.bbq.infrastructure.repository.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.demo.bbq.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.repository.menuoption.entity.MenuOptionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@AutoConfigureTestDatabase(replace = NONE) //use real database
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MenuOptionRepositoryTest {

  @Autowired
  private MenuOptionRepository repository;

  private List<MenuOptionEntity> expectedSavedMenuOptionList;

  private JsonSerializer jsonSerializer;

  @BeforeEach
  public void setup() {
    jsonSerializer = new JsonSerializer(new ObjectMapper());
    expectedSavedMenuOptionList = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class);
    repository.saveAll(expectedSavedMenuOptionList);
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void findAll() {
    String expected = new Gson().toJson(expectedSavedMenuOptionList);
    String actual = new Gson().toJson(repository.findAll());
    assertEquals(expected, actual);
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void findByProductCode() {
    MenuOptionEntity expectedSavedMenuOptionThreeEntity = expectedSavedMenuOptionList.get(0);
    String expected = new Gson().toJson(expectedSavedMenuOptionThreeEntity);
    String actual = new Gson().toJson(repository.findByProductCode("MENU0001").get());
    assertEquals(expected, actual);
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void findByCategory() {
    List<MenuOptionEntity> expectedFilteredMenuOptionListEntity = expectedSavedMenuOptionList
        .stream()
        .filter(menuOption -> menuOption.getCategory().equals("MAIN"))
        .collect(Collectors.toList());

    String expected = new Gson().toJson(expectedFilteredMenuOptionListEntity);
    String actual = new Gson().toJson(repository.findByCategory("MAIN"));
    assertEquals(expected, actual);
  }

  //  @Rollback(value = false) // no revertir los cambios en BD tras la ejecución del test
  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void save() {
    int rowsNumberBefore = repository.findAll().size();

    MenuOptionEntity menuOptionToSave = jsonSerializer.readListFromFile("data/menuoption/MenuOptionEntity_Array.json", MenuOptionEntity[].class).get(0);
    menuOptionToSave.setId(4L);
    menuOptionToSave.setProductCode("MENU0004");
    repository.save(menuOptionToSave);

    int rowsNumberAfter = repository.findAll().size();

    MenuOptionEntity savedMenuOption = repository.findByProductCode("MENU0004").get();
    String expected = new Gson().toJson(menuOptionToSave);
    String actual = new Gson().toJson(savedMenuOption);

    assertEquals(rowsNumberBefore + 1, rowsNumberAfter);
    assertEquals(expected, actual);
    assertNotNull(savedMenuOption);
    assertNotNull(savedMenuOption.getId());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void deleteByProductCode() {
    int rowsNumberBefore = repository.findAll().size();
    String PRODUCT_CODE = "MENU0001";
    boolean isExistBefore = repository.findByProductCode(PRODUCT_CODE).isPresent();
    repository.deleteByProductCode(PRODUCT_CODE);
    int rowsNumberAfter = repository.findAll().size();
    boolean isExistAfter = repository.findByProductCode(PRODUCT_CODE).isPresent();

    assertEquals(rowsNumberBefore, rowsNumberAfter + 1);
    assertTrue(isExistBefore);
    assertFalse(isExistAfter);
  }

}