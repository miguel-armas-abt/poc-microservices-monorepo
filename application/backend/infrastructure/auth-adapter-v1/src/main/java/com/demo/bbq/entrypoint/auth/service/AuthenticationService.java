package com.demo.bbq.entrypoint.auth.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.auth.repository.authprovider.*;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final ApplicationProperties properties;
  private final AuthTokenRepository authTokenRepository;
  private final LogoutRepository logoutRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserInfoRepository userInfoRepository;
  private final JsonKeyWrappingRepository jsonKeyWrappingRepository;

  public Single<TokenResponseWrapper> getToken(String username, String password) {
    return authTokenRepository.getToken(properties, username, password);
  }

  public Completable logout(String refreshToken) {
    return logoutRepository.logout(properties, refreshToken)
        .onErrorResumeNext(throwable -> Completable.error(new AuthorizationException("UnableLogout", throwable.getMessage())));
  }

  public Single<TokenResponseWrapper> refreshToken(String refreshToken) {
    return refreshTokenRepository.refreshToken(properties, refreshToken)
        .onErrorResumeNext(throwable -> Single.error(new AuthorizationException("UnableRefresh")));
  }

  public Single<UserInfoResponseWrapper> getUserInfo(String authToken) {
    return userInfoRepository.getUserInfo(authToken);
  }

  public Map<String, Integer> getRoles(String authToken) {
      DecodedJWT jwt = JWT.decode(authToken.replace("Bearer", "").trim());

      verifyJwtAlgorithm(jwt);
      verifyJwtExpiration(jwt);

      List<String> roles = ((List) jwt.getClaim("realm_access").asMap().get("roles"));
      Map<String, Integer> rolesMap = new HashMap();
      for (String actualRol : roles) {
        rolesMap.put(actualRol, actualRol.length());
      }
      return rolesMap;
  }

  private void verifyJwtAlgorithm(DecodedJWT jwt) {
    try {
      Jwk jwk = jsonKeyWrappingRepository.getJwk();
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
      algorithm.verify(jwt);
    } catch (Exception ex) {
      throw new AuthorizationException("InvalidJWT", ex.getMessage());
    }
  }

  private void verifyJwtExpiration(DecodedJWT jwt) {
    Date expiryDate = jwt.getExpiresAt();
    if (expiryDate.before(new Date())) {
      throw new AuthorizationException("ExpiredToken");
    }
  }

}
