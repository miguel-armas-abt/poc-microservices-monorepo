package com.demo.bbq.business.menuoption.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.demo.bbq.business.menuoption.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.support.util.JsonFileReader;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

// @AutoConfigureTestDatabase(replace = NONE) //use real database
@DataJpaTest
@ActiveProfiles("test") //use application-test.yaml
public class MenuOptionEntityRepositoryTest {

  @Autowired
  private MenuOptionRepository repository;

  private List<MenuOptionEntity> expectedSavedMenuOptionListEntity;

  @BeforeEach
  void setup() throws IOException {
    expectedSavedMenuOptionListEntity = JsonFileReader
        .getList("data/model/menuoption/entity/MenuOption_list.json", MenuOptionEntity[].class);

    // save test data
    repository.saveAll(expectedSavedMenuOptionListEntity);
  }

  @Test
  public void findAll() {
    String expected = new Gson().toJson(expectedSavedMenuOptionListEntity);
    String actual = new Gson().toJson(repository.findAll());
    assertEquals(expected, actual);
  }

  @Test
  public void findById() {
    MenuOptionEntity expectedSavedMenuOptionThreeEntity = expectedSavedMenuOptionListEntity.get(2);
    String expected = new Gson().toJson(expectedSavedMenuOptionThreeEntity);
    String actual = new Gson().toJson(repository.findById(3L).get());
    assertEquals(expected, actual);
  }

  @Test
  public void findByCategory() {
    List<MenuOptionEntity> expectedFilteredMenuOptionListEntity = expectedSavedMenuOptionListEntity
        .stream()
        .filter(menuOption -> menuOption.getCategory().equals("main-dish"))
        .collect(Collectors.toList());

    String expected = new Gson().toJson(expectedFilteredMenuOptionListEntity);
    String actual = new Gson().toJson(repository.findByCategory("main-dish"));
    assertEquals(expected, actual);
  }

//  @Rollback(value = false) // no revertir los cambios en BD tras la ejecución del test
  @Test
  public void save() throws IOException {
    int rowsNumberBefore = repository.findAll().size();
    MenuOptionEntity expectedMenuOptionEntity = JsonFileReader.getAnElement("data/model/menuoption/entity/MenuOption.json", MenuOptionEntity.class);
    repository.save(expectedMenuOptionEntity);
    int rowsNumberAfter = repository.findAll().size();
    MenuOptionEntity actualMenuOptionEntity = repository.findById(7L).get();

    String expected = new Gson().toJson(expectedMenuOptionEntity);
    String actual = new Gson().toJson(actualMenuOptionEntity);

    assertEquals(rowsNumberBefore + 1, rowsNumberAfter);
    assertEquals(expected, actual);
    assertNotNull(actualMenuOptionEntity);
    assertNotNull(actualMenuOptionEntity.getId());
  }

  @Test
  public void deleteById() {
    int rowsNumberBefore = repository.findAll().size();
    Long id = 1L;
    boolean isExistBefore = repository.findById(id).isPresent();
    repository.deleteById(1L);
    int rowsNumberAfter = repository.findAll().size();
    boolean isExistAfter = repository.findById(id).isPresent();

    assertEquals(rowsNumberBefore, rowsNumberAfter + 1);
    assertTrue(isExistBefore);
    assertFalse(isExistAfter);
  }

}