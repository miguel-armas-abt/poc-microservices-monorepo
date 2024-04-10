package com.demo.bbq.infrastructure.apigateway.domain.repository.authadapter;

import com.demo.bbq.infrastructure.apigateway.domain.repository.authadapter.properties.AuthAdapterRestClientProperties;
import com.demo.bbq.infrastructure.apigateway.infrastructure.config.restclient.WebClientFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Repository
public class AuthAdapterRepository {

  private final WebClient webClient;
  private final AuthAdapterRestClientProperties properties;

  public AuthAdapterRepository(WebClientFactory webClientFactory,
                               AuthAdapterRestClientProperties properties) {
    this.webClient = webClientFactory.createWebClient();
    this.properties = properties;
  }

  public Flux<HashMap<String, Integer>> getRoles(String authToken) {
    return webClient.get()
        .uri(properties.getBaseURL().concat("/roles"))
        .headers(buildHttpHeaders().andThen(buildAuthorizationHeader(authToken)))
        .retrieve()
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

}
