package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.properties.KeycloackProperties;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderConnector {

  private final KeycloackProperties properties;
  private final TokenApi tokenApi;

  private final LogoutApi logoutApi;

  private final UserInfoApi userInfoApi;

  private final RefreshApi refreshApi;

  public Single<TokenResponse> getToken(String username, String password) {
    return tokenApi.getToken(username, password, properties.getClientId(), properties.getGrantType(), properties.getClientSecret(), properties.getScope());
  }

  public Completable logout(String refreshToken) {
    return logoutApi.logout(properties.getClientId(), properties.getClientSecret(), refreshToken);
  }

  public Single<UserInfoResponse> getUserInfo(String authToken) {
    return userInfoApi.getUserInfo(authToken);
  }

  public Single<TokenResponse> refreshToken(String refreshToken) {
    return refreshApi.refresh(properties.getClientId(), properties.getGrantTypeRefresh(), refreshToken);
  }
}
