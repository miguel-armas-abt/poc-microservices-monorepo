package com.demo.poc.commons.core.validations;

import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.DisplayName;

class BodyValidatorTest {
  private final BodyValidator validator = new BodyValidator();

  @AllArgsConstructor
  @Getter
  @Setter
  public static class DummyDto {
    @NotBlank
    private final String field;
  }

  @ParameterizedTest(name = "#{index} – field=''{0}'' → isValid={1}")
  @CsvSource(value = {
      "hello,true",
      "'',false",
      "NULL,false"
  }, nullValues = "NULL")
  @DisplayName("Given DummyDto, when isValid, then returns expected boolean")
  void givenDummyDtoWhenIsValid_thenReturnsExpected(String fieldValue, boolean expected) {
    // Arrange
    DummyDto dto = new DummyDto(fieldValue);

    // Act
    boolean actual = validator.isValid(dto);

    // Assert
    Assertions.assertEquals(expected, actual);
  }

  @ParameterizedTest(name = "#{index} – field=''{0}'' → shouldError={1}")
  @CsvSource(value = {
      "hello,false",
      "'',true",
      "NULL,true"
  }, nullValues = "NULL")
  @DisplayName("Given DummyDto, when validateAndGet, then emits or errors appropriately")
  void givenDummyDto_whenValidateAndGet_thenEmitsOrErrors(String fieldValue, boolean shouldError) {
    // Arrange
    DummyDto dto = new DummyDto(fieldValue);

    // Act
    Single<DummyDto> single = validator.validateAndGet(dto);
    TestObserver<DummyDto> ts = single.test();

    // Assert
    if (!shouldError) {
      ts.assertComplete()
          .assertNoErrors()
          .assertValue(emitted -> emitted.getField().equals(fieldValue));
    } else {
      ts.assertError(InvalidFieldException.class)
          .assertError(error -> error.getMessage().contains("The field"));
    }
  }
}