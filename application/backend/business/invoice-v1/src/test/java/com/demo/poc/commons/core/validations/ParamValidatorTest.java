package com.demo.poc.commons.core.validations;

import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParamValidatorTest {

  @Getter
  @RequiredArgsConstructor
  public static class DummyParams {
    @NotNull
    private final String id;
  }

  public static class DummyParamMapper implements ParamMapper {
    @Override
    public Object map(Map<String, String> params) {
      return new DummyParams(params.get("id"));
    }

    @Override
    public boolean supports(Class<?> paramClass) {
      return DummyParams.class.equals(paramClass);
    }
  }

  private final BodyValidator bodyValidator = new BodyValidator();

  private final ParamValidator validator = new ParamValidator(List.of(new DummyParamMapper()), bodyValidator);

  @ParameterizedTest(name = "#{index} – paramValue=''{0}''")
  @CsvSource({"01", "02"})
  @DisplayName("Given valid paramsMap, when validateAndGet, then emits mapped and validated object")
  void givenValidParamsMap_whenValidateAndGet_thenEmitsMappedObject(String paramValue) {
    // Arrange
    Map<String, String> paramsMap = Map.of("id", paramValue);

    // Act
    Mono<DummyParams> resultMono = validator.validateAndGet(paramsMap, DummyParams.class);

    // Assert
    StepVerifier.create(resultMono)
        .assertNext(param -> assertEquals(paramValue, param.getId()))
        .verifyComplete();
  }

  @DisplayName("Given missing params, when validateAndGet, then errors InvalidFieldException")
  @Test
  void givenMissingParams_whenValidateAndGet_thenErrorsInvalidFieldException() {
    // Arrange
    Map<String, String> paramsMap = Map.of();

    // Act & Assert
    StepVerifier.create(validator.validateAndGet(paramsMap, DummyParams.class))
        .expectError(InvalidFieldException.class)
        .verify();
  }

  @DisplayName("Given no mapper supports class, when validateAndGet, then throws NoSuchParamMapperException")
  @Test
  void givenNoMapper_whenValidateAndGet_thenThrowsNoSuchParamMapperException() {
    // Arrange
    ParamValidator emptyValidator = new ParamValidator(List.of(), bodyValidator);

    // Act & Assert
    assertThrows(NoSuchParamMapperException.class,
        () -> emptyValidator
            .validateAndGet(Map.of("key", "value"), DummyParams.class)
            .block());
  }
}