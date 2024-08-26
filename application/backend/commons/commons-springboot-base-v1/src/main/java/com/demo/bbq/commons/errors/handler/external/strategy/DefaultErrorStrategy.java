package com.demo.bbq.commons.errors.handler.external.strategy;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.toolkit.serialization.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class DefaultErrorStrategy implements RestClientErrorStrategy {

  private final JsonSerializer serializer;

  @Override
  public Optional<Pair<String, String>> getCodeAndMessage(String jsonBody) {
    return serializer.writeCodeAndMessage(jsonBody, ErrorDTO.class, errorMapper);
  }

  private final Function<ErrorDTO, Pair<String, String>> errorMapper = errorDetail
      -> Pair.of(errorDetail.getCode(), errorDetail.getMessage());

  @Override
  public boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass) {
    return wrapperClass.isAssignableFrom(ErrorDTO.class);
  }

}
