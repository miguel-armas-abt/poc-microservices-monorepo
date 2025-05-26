package com.demo.poc.entrypoint.auth.repository.logout;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.utils.FormDataFiller;
import java.util.Map;

import com.demo.poc.entrypoint.auth.repository.error.KeycloakError;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
public class LogoutRepository {

  private static final String SERVICE_NAME = "logout";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public LogoutRepository(ApplicationProperties properties,
                                RestClientErrorHandler errorHandler,
                                WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<Void> logout(Map<String, String> headers, String refreshToken) {
    return webClient.post()
        .uri(restClient.getRequest().getEndpoint().concat("logout"))
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromFormData(fillFormData(restClient, refreshToken)))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(Void.class)
        .mapNotNull(HttpEntity::getBody);
  }

  static MultiValueMap<String, String> fillFormData(RestClient restClient, String refreshToken) {
    Map<String, String> providedParams = restClient.getRequest().getFormData();
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, KeycloakError.class, SERVICE_NAME);
  }
}
