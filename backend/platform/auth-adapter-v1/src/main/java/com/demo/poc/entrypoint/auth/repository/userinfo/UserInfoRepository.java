package com.demo.poc.entrypoint.auth.repository.userinfo;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.auth.repository.error.KeycloakError;
import com.demo.poc.entrypoint.auth.repository.userinfo.wrapper.UserInfoResponseWrapper;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
public class UserInfoRepository {

  private static final String SERVICE_NAME = "user-info";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;

  public UserInfoRepository(ApplicationProperties properties,
                                RestClientErrorHandler errorHandler,
                                WebClientFactory webClientFactory) {
    this.errorHandler = errorHandler;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<UserInfoResponseWrapper> getUserInfo(Map<String, String> headers) {
    return webClient.post()
        .uri(restClient.getRequest().getEndpoint().concat("userinfo"))
        .headers(fillHeaders(restClient.getRequest().getHeaders(), headers))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(UserInfoResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, KeycloakError.class, SERVICE_NAME);
  }
}
