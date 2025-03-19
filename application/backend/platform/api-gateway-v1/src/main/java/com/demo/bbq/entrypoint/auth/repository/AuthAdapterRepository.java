package com.demo.bbq.entrypoint.auth.repository;

import com.demo.bbq.commons.properties.base.restclient.HeaderTemplate;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandler;
import com.demo.bbq.commons.restclient.WebClientFactory;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class AuthAdapterRepository {

  private static final String SERVICE_NAME = "auth-adapter-v1";

  private final ApplicationProperties properties;
  private final ExternalErrorHandler externalErrorHandler;
  private final WebClientFactory webClientFactory;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME), SERVICE_NAME);
  }

  public Mono<HashMap<String, Integer>> getRoles(Map<String, String> headers) {
    return webClient.get()
        .uri(properties.getRestClients().get(SERVICE_NAME).getRequest().getEndpoint().concat("/roles"))
        .headers(fillHeaders(getHeaderTemplate(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> externalErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME))
        .toEntity(HashMap.class)
        .mapNotNull(HttpEntity::getBody)
        .mapNotNull(this::castHashMapToIntegerValues);
  }

  private HashMap<String, Integer> castHashMapToIntegerValues(HashMap<String, ?> input) {
    HashMap<String, Integer> result = new HashMap<>();
    input.forEach((key, value) -> result.put(key, (Integer) value));
    return result;
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.getRestClients().get(SERVICE_NAME).getRequest().getHeaders();
  }
}
