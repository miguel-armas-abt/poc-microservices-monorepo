package com.demo.poc.entrypoint.auth.repository.error;

import com.demo.poc.commons.core.restclient.error.RestClientErrorExtractor;
import com.demo.poc.commons.core.restclient.error.RestClientErrorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class KeycloakErrorExtractor implements RestClientErrorExtractor {

  private final RestClientErrorMapper mapper;

  @Override
  public Optional<Map.Entry<String, String>> getCodeAndMessage(String jsonBody) {
    return mapper.getCodeAndMessage(jsonBody, KeycloakError.class, errorMapper);
  }

  private final Function<KeycloakError, Map.Entry<String, String>> errorMapper = errorDetail
      -> Map.of(errorDetail.getError(), errorDetail.getErrorDescription()).entrySet().iterator().next();

  @Override
  public boolean supports(Class<?> wrapperClass) {
    return wrapperClass.isAssignableFrom(KeycloakError.class);
  }

}
