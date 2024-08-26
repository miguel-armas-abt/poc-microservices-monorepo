package com.demo.bbq.commons.toolkit.validator.params;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import com.demo.bbq.commons.toolkit.validator.utils.ParamReflectiveMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ParamReflectiveMapperTest {

  @ParameterizedTest
  @CsvSource(value = {
      "web, abcde, Bearer abcde, application/json, application/json",
  }, delimiter = ',')
  @DisplayName("Given a headers map, when mapping, then result is DummyHeaders")
  void givenHeadersMap_WhenMapping_ThenResultIsDummyHeaders(String channelId,
                                                            String traceId,
                                                            String authorization,
                                                            String contentType,
                                                            String accept) {
    //Arrange
    Map<String, String> headers = Map.of(
        "channel-id", channelId,
        "trace-id", traceId,
        "Authorization", authorization,
        "Content-Type", contentType,
        "Accept", accept
    );

    //Act
    DummyHeader dummyHeader = ParamReflectiveMapper.mapParam(headers, DummyHeader.class, true);

    //Assert
    assertEquals(channelId, dummyHeader.getChannelId());
    assertEquals(traceId, dummyHeader.getTraceId());
    assertEquals(authorization, dummyHeader.getAuthorization());
    assertEquals(contentType, dummyHeader.getContentType());
    assertEquals(accept, dummyHeader.getAccept());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "12345, abcde",
  }, delimiter = ',')
  @DisplayName("Given a query param map, when mapping, then result is DummyQueryParam")
  void givenHeadersMap_WhenMapping_ThenResultIsDummyQueryParam(String userId, String productCode){
    //Arrange
    Map<String, String> queryParams = Map.of(
        "userId", userId,
        "productCode", productCode
    );

    //Act
    DummyQueryParam dummyQueryParam = ParamReflectiveMapper.mapParam(queryParams, DummyQueryParam.class, false);

    //Assert
    assertEquals(userId, dummyQueryParam.getUserId());
    assertEquals(productCode, dummyQueryParam.getProductCode());
  }
}