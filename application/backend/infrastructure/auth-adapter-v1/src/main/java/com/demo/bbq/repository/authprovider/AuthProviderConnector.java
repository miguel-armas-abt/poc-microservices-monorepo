package com.demo.bbq.repository.authprovider;

import static com.demo.bbq.repository.authprovider.config.AuthProviderRestClientConfig.SERVICE_NAME;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.authprovider.wrapper.UserInfoResponseWrapper;
import com.demo.bbq.repository.authprovider.wrapper.TokenResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProviderConnector {

  private final ServiceConfigurationProperties properties;
  private final AuthProviderRepository authProviderRepository;

  public Single<TokenResponseWrapper> getToken(String username, String password) {
    return authProviderRepository
        .getToken(
            username,
            password,
            searchVariable("client-id"),
            searchVariable("authorization-grant-type"),
            searchVariable("client-secret"),
            searchVariable("scope"));
  }

  public Completable logout(String refreshToken) {
    return authProviderRepository
        .logout(
            searchVariable("client-id"),
            searchVariable("client-secret"),
            refreshToken);
  }

  public Single<UserInfoResponseWrapper> getUserInfo(String authToken) {
    return authProviderRepository
        .getUserInfo(authToken);
  }

  public Single<TokenResponseWrapper> refreshToken(String refreshToken) {
    return authProviderRepository
        .refresh(
            searchVariable("client-id"),
            searchVariable("authorization-grant-type-refresh"),
            refreshToken);
  }

  private String searchVariable(String variableName) {
    return properties.searchVariables(SERVICE_NAME).get(variableName);
  }
}
