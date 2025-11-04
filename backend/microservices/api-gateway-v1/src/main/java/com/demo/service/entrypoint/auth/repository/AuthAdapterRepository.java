package com.demo.service.entrypoint.auth.repository;

import com.demo.commons.errors.dto.ErrorDto;
import com.demo.commons.properties.restclient.RestClient;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import com.demo.service.commons.properties.ApplicationProperties;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.demo.commons.restclient.utils.HeadersFiller.fillHeaders;


@Repository
public class AuthAdapterRepository {

  private static final String SERVICE_NAME = "auth-adapter-v1";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public AuthAdapterRepository(ApplicationProperties properties,
                              RestClientErrorHandler errorHandler,
                              WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<HashMap<String, Integer>> getRoles(Map<String, String> headers) {
    return webClient.get()
        .uri(restClient.getRequest().getEndpoint().concat("/roles"))
        .headers(x -> fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> errorHandler.handleError(clientResponse, ErrorDto.class, SERVICE_NAME))
        .toEntity(HashMap.class)
        .mapNotNull(HttpEntity::getBody)
        .mapNotNull(this::castHashMapToIntegerValues);
  }

  private HashMap<String, Integer> castHashMapToIntegerValues(HashMap<String, ?> input) {
    HashMap<String, Integer> result = new HashMap<>();
    input.forEach((key, value) -> result.put(key, (Integer) value));
    return result;
  }
}
