package com.demo.bbq.commons.tracing.logging.injector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ThreadContextUtilsTest {

  @ParameterizedTest
  @DisplayName("Given headers map When extractTraceHeaders Then return trace headers in camelCase")
  @CsvSource(value = {
      "trace-id:xyz; traceId:xyz",
      "trace-id:123,parent-id:456; traceId:123,parentId:456",
  }, delimiter = ';')
  public void givenHeaders_WhenExtractTraceHeaders_ThenReturnTraceFieldsFromHeadersInCamelCase(String inputHeaders, String expectedOutput) {
    //Arrange
    Map<String, String> headers = parseHeaders(inputHeaders);

    //Act
    Map<String, String> result = ThreadContextUtils.extractTraceFieldsFromHeaders(headers);

    //Assert
    Map<String, String> expectedMap = parseHeaders(expectedOutput);
    assertEquals(expectedMap, result);
  }

  private Map<String, String> parseHeaders(String headerString) {
    return Optional.ofNullable(headerString)
        .filter(s -> !s.isEmpty())
        .stream()
        .flatMap(s -> Arrays.stream(s.split(",")))
        .map(entry -> entry.split(":"))
        .filter(keyValue -> keyValue.length == 2)
        .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1]));
  }

}