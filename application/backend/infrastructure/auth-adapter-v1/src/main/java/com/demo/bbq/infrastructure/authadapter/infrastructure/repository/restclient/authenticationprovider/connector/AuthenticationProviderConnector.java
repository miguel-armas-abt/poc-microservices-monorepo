package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.properties.KeycloackProperties;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderConnector {

  private final KeycloackProperties properties;
  private final AuthenticationProviderApi authenticationProviderApi;

  public Single<TokenResponse> getToken(String username, String password) {
    return authenticationProviderApi.getToken(username, password, properties.getClientId(), properties.getGrantType(), properties.getClientSecret(), properties.getScope());
  }

  public Completable logout(String refreshToken) {
    return authenticationProviderApi.logout(properties.getClientId(), properties.getClientSecret(), refreshToken);
  }

  public Single<UserInfoResponse> getUserInfo(String authToken) {
    return authenticationProviderApi.getUserInfo(authToken);
  }

  public Single<TokenResponse> refreshToken(String refreshToken) {
    return authenticationProviderApi.refresh(properties.getClientId(), properties.getGrantTypeRefresh(), refreshToken);
  }
}
