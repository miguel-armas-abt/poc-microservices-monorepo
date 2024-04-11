package com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider;

import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.wrapper.UserInfoResponseWrapper;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.config.AuthProviderRestClientProperties;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.wrapper.TokenResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProviderConnector {

  private final AuthProviderRestClientProperties properties;
  private final AuthProviderRepository authProviderRepository;

  public Single<TokenResponseWrapper> getToken(String username, String password) {
    return authProviderRepository.getToken(username, password, properties.getClientId(), properties.getGrantType(), properties.getClientSecret(), properties.getScope());
  }

  public Completable logout(String refreshToken) {
    return authProviderRepository.logout(properties.getClientId(), properties.getClientSecret(), refreshToken);
  }

  public Single<UserInfoResponseWrapper> getUserInfo(String authToken) {
    return authProviderRepository.getUserInfo(authToken);
  }

  public Single<TokenResponseWrapper> refreshToken(String refreshToken) {
    return authProviderRepository.refresh(properties.getClientId(), properties.getGrantTypeRefresh(), refreshToken);
  }
}
