package com.demo.bbq.repository.authadapter;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.config.errors.external.ExternalServiceErrorHandler;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class AuthAdapterRepository {

  private static final String SERVICE_NAME = "auth-adapter-v1";

  private final WebClient webClient;
  private final ServiceConfigurationProperties properties;
  private final ExternalServiceErrorHandler externalServiceErrorHandler;

  public Flux<HashMap<String, Integer>> getRoles(String authToken) {
    return webClient.get()
        .uri(properties.getRestClients().get(SERVICE_NAME).getRequest().getEndpoint().concat("/roles"))
        .headers(buildHttpHeaders().andThen(buildAuthorizationHeader(authToken)))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> externalServiceErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME))
        .bodyToFlux(HashMap.class)
        .map(this::castHashMapToIntegerValues);
  }

  private static Consumer<HttpHeaders> buildHttpHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Accept", "application/json");
    return httpHeaders -> Optional.of(headers)
        .ifPresent(headerMap -> headerMap.forEach(httpHeaders::set));
  }

  private static Consumer<HttpHeaders> buildAuthorizationHeader(String authToken) {
    return httpHeaders -> httpHeaders.set("Authorization", "Bearer ".concat(authToken));
  }

  private HashMap<String, Integer> castHashMapToIntegerValues(HashMap<String, ?> input) {
    HashMap<String, Integer> result = new HashMap<>();
    input.forEach((key, value) -> result.put(key, (Integer) value));
    return result;
  }

  public Map<String, String> getVariables() {
    return properties.getRestClients().get(SERVICE_NAME).getVariables();
  }

}
