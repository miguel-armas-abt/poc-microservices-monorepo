package com.demo.bbq.repository.authadapter;

import static com.demo.bbq.utils.restclient.headers.HeadersBuilderUtil.buildHeaders;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.config.errors.external.ExternalServiceErrorHandler;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import java.util.HashMap;
import java.util.Map;
import com.demo.bbq.utils.properties.dto.HeaderTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

  public Flux<HashMap<String, Integer>> getRoles(ServerHttpRequest serverRequest) {
    return webClient.get()
        .uri(properties.getRestClients().get(SERVICE_NAME).getRequest().getEndpoint().concat("/roles"))
        .headers(buildHeaders(getHeaderTemplate(), serverRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, clientResponse -> externalServiceErrorHandler.handleError(clientResponse, ErrorDTO.class, SERVICE_NAME))
        .bodyToFlux(HashMap.class)
        .map(this::castHashMapToIntegerValues);
  }

  private HashMap<String, Integer> castHashMapToIntegerValues(HashMap<String, ?> input) {
    HashMap<String, Integer> result = new HashMap<>();
    input.forEach((key, value) -> result.put(key, (Integer) value));
    return result;
  }

  public Map<String, String> getVariables() {
    return properties.getRestClients().get(SERVICE_NAME).getVariables();
  }

  private HeaderTemplate getHeaderTemplate() {
    return properties.getRestClients().get(SERVICE_NAME).getRequest().getHeaders();
  }
}
