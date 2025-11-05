package com.demo.service.entrypoint.auth.repository.authtoken;

import com.demo.commons.properties.restclient.RestClient;
import com.demo.commons.restclient.WebClientFactory;
import com.demo.commons.restclient.error.RestClientErrorHandler;
import com.demo.commons.restclient.utils.FormDataFiller;
import com.demo.commons.restclient.utils.HttpHeadersFiller;
import com.demo.service.commons.properties.ApplicationProperties;
import com.demo.service.entrypoint.auth.repository.authtoken.wrapper.TokenResponseWrapper;
import java.util.Map;

import com.demo.service.entrypoint.auth.repository.error.KeycloakError;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class AuthTokenRepository {

  private static final String SERVICE_NAME = "auth-token";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public AuthTokenRepository(ApplicationProperties properties,
                             RestClientErrorHandler errorHandler,
                             WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<TokenResponseWrapper> getToken(Map<String, String> headers,
                                             String username, String password) {
    return webClient.post()
        .uri(restClient.getRequest().getEndpoint().concat("token"))
        .headers(HttpHeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), headers))
        .body(BodyInserters.fromFormData(fillFormData(restClient, username, password)))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(TokenResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  static MultiValueMap<String, String> fillFormData(RestClient restClient, String username, String password) {
    Map<String, String> providedParams = restClient.getRequest().getFormData();
    Map<String, String> currentParams = Map.of("username", username, "password", password);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    FormDataFiller.fillFormData(providedParams, currentParams).forEach(formData::add);
    return formData;
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, KeycloakError.class, SERVICE_NAME);
  }
}
