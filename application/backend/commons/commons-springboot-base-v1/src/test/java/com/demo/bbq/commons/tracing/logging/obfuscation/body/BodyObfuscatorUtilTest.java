package com.demo.bbq.commons.tracing.logging.obfuscation.body;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BodyObfuscatorUtilTest {

  private static final String NESTED_FIELD = "{\"user\":{\"password\":\"mySecretPass\"}}";
  private static final String NESTED_FIELD_OBFUSCATED = "{\"user\":{\"password\":\"myS****ass\"}}";

  private static final String FIELD_IN_ARRAY = "{\"user\":{\"phones\":[{\"number\":\"1234567890\"},{\"number\":\"0987654321\"}]}}";
  private static final String FIELD_IN_ARRAY_OBFUSCATED = "{\"user\":{\"phones\":[{\"number\":\"123****890\"},{\"number\":\"098****321\"}]}}";

  @ParameterizedTest
  @CsvSource(value = {
      NESTED_FIELD + "; user.password; " + NESTED_FIELD_OBFUSCATED,
      FIELD_IN_ARRAY + "; user.phones[*].number; " + FIELD_IN_ARRAY_OBFUSCATED,
  }, delimiter = ';')
  @DisplayName("Given a valid JSON, when obfuscate, then result contains the obfuscated fields")
  void givenValidJson_WhenObfuscate_ThenFieldsAreObfuscated(String inputJson, String fieldToObfuscate, String expectedJson) {
    //Arrange
    ObfuscationTemplate template = new ObfuscationTemplate();
    template.setBodyFields(Set.of(fieldToObfuscate));

    //Act
    String result = BodyObfuscatorUtil.process(template, inputJson);

    //Assert
    assertEquals(expectedJson, result);
  }

  @Test
  @DisplayName("Given an empty value, when obfuscate, then result is the same value")
  void givenEmpty_WhenObfuscate_ThenResultIsSameValue() {
    //Arrange
    ObfuscationTemplate template = new ObfuscationTemplate();
    template.setBodyFields(Set.of("user.password"));

    //Act
    String result = BodyObfuscatorUtil.process(template, StringUtils.EMPTY);

    //Assert
    assertEquals(StringUtils.EMPTY, result);
  }

  @Test
  @DisplayName("Given no fields to obfuscate, when obfuscate, then result is same JSON ")
  void givenNoFieldsToObfuscate_WhenObfuscate_ThenResultIsSameJson() {
    //Arrange
    ObfuscationTemplate template = new ObfuscationTemplate();
    template.setBodyFields(Set.of());

    //Act
    String result = BodyObfuscatorUtil.process(template, NESTED_FIELD);

    //Assert
    assertEquals(NESTED_FIELD, result);
  }

  @Test
  @DisplayName("Given JSON that no matching fields, when obfuscate, then result is same JSON")
  void givenJsonThatNoMatchingFields_WhenObfuscate_ThenResultIsSameJson() {
    //Arrange
    String inputJson = "{\"user\":{\"username\":\"john_doe\"}}";
    ObfuscationTemplate template = new ObfuscationTemplate();
    template.setBodyFields(Set.of("user.password"));

    //Act
    String result = BodyObfuscatorUtil.process(template, inputJson);

    //Assert
    assertEquals(inputJson, result);
  }

  @Test
  @DisplayName("Given JSON with empty array, when obfuscate, then result is same JSON")
  void givenJsonWithEmptyArray_WhenObfuscate_ThenResultIsSameJson() {
    //Arrange
    String inputJson = "{\"user\":{\"phones\":[]}}";
    ObfuscationTemplate template = new ObfuscationTemplate();
    template.setBodyFields(Set.of("user.phones[*].number"));

    //Act
    String result = BodyObfuscatorUtil.process(template, inputJson);

    //Assert
    assertEquals(inputJson, result);
  }
}