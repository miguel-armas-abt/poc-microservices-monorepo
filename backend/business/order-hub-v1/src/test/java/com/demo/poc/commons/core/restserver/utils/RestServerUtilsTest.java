package com.demo.poc.commons.core.restserver.utils;

import com.demo.poc.commons.core.tracing.enums.TraceParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RestServerUtilsTest {

  @ParameterizedTest(name = "#{index} – Header [{0} = {1}]")
  @CsvSource({
      "h1,v1",
      "h2,v2"
  })
  @DisplayName("Given ServerRequest with headers, when extractHeadersAsMap, then returns the correct Map")
  void givenServerRequestWithHeaders_whenExtractHeadersAsMap_thenReturnsCorrectMap(String headerName,
                                                                                   String headerValue) {
    // Arrange
    ServerRequest serverRequest = MockServerRequest.builder()
        .header(headerName, headerValue)
        .build();

    // Act
    Map<String, String> result = RestServerUtils.extractHeadersAsMap(serverRequest);

    // Assert
    assertEquals(1, result.size());
    assertEquals(headerValue, result.get(headerName));
  }

  @ParameterizedTest(name = "#{index} – Header [{0} = {1}]")
  @CsvSource({
      "h1,v1",
      "h2,v2"
  })
  @DisplayName("Given ServerHttpRequest with headers, when extractHeadersAsMap, then returns the correct Map")
  void givenServerHttpRequestWithHeaders_whenExtractHeadersAsMap_thenReturnsCorrectMap(String headerName,
                                                                                       String headerValue) {
    // Arrange
    ServerHttpRequest httpRequest = MockServerHttpRequest.get("/")
        .header(headerName, headerValue)
        .build();

    // Act
    Map<String, String> result = RestServerUtils.extractHeadersAsMap(httpRequest);

    // Assert
    assertEquals(1, result.size());
    assertEquals(headerValue, result.get(headerName));
  }

  @ParameterizedTest(name = "#{index} – QueryParam [{0} = {1}] → {2}")
  @CsvSource(value = {
      "p1,value,value",
  })
  @DisplayName("Given ServerRequest with query params, when extractQueryParamsAsMap, then returns joined values")
  void givenServerRequestWithQueryParams_whenExtractQueryParamsAsMap_thenReturnsJoinedValues(String paramKey,
                                                                                             String valuesStr,
                                                                                             String expectedJoined) {
    // Arrange
    ServerRequest serverRequest = MockServerRequest.builder()
        .queryParam(paramKey, valuesStr)
        .build();

    // Act
    Map<String, String> result = RestServerUtils.extractQueryParamsAsMap(serverRequest);

    // Assert
    assertEquals(1, result.size());
    assertEquals(expectedJoined, result.get(paramKey));
  }

  @ParameterizedTest(name = "#{index} – traceParent = {0}")
  @CsvSource(value = {
      "00-0123456789abcdef0123456789abcdef-0123456789abcdef-01",
      "NULL"
  }, nullValues = "NULL")
  @DisplayName("Given requestHeaders, when buildResponseHeaders, then sets or omits traceId")
  void givenRequestHeadersWhen_buildResponseHeadersThenSetsOrOmitsTraceId(String traceParent) {
    // Arrange
    var builder = MockServerRequest.builder();
    if (Objects.nonNull(traceParent)) {
      builder.header(TraceParam.TRACE_PARENT.getKey(), traceParent);
    }
    ServerRequest serverRequest = builder.build();
    ServerRequest.Headers reqHeaders = serverRequest.headers();
    Consumer<HttpHeaders> responseHeadersBuilder = RestServerUtils.buildResponseHeaders(reqHeaders);
    HttpHeaders responseHeaders = new HttpHeaders();

    // Act
    responseHeadersBuilder.accept(responseHeaders);

    // Assert
    if (Objects.nonNull(traceParent)) {
      String expected = TraceParam.Util.getTraceId.apply(traceParent);
      assertEquals(expected, responseHeaders.getFirst(TraceParam.TRACE_ID.getKey()));
    }
    else {
      assertNull(responseHeaders.getFirst(TraceParam.TRACE_ID.getKey()));
    }
  }
}