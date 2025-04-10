package com.demo.poc.commons.core.restclient.error.extractor.poc;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.restclient.error.RestClientErrorExtractor;
import com.demo.poc.commons.core.serialization.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class DefaultErrorExtractor implements RestClientErrorExtractor {

  private final JsonSerializer serializer;

  @Override
  public Optional<Pair<String, String>> getCodeAndMessage(String jsonBody) {
    return serializer.writeCodeAndMessage(jsonBody, ErrorDto.class, errorMapper);
  }

  private final Function<ErrorDto, Pair<String, String>> errorMapper = errorDetail
      -> Pair.of(errorDetail.getCode(), errorDetail.getMessage());

  @Override
  public boolean supports(Class<?> errorWrapperClass) {
    return errorWrapperClass.isAssignableFrom(ErrorDto.class);
  }

}
