package com.demo.bbq.infrastructure.authadapter.application.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.AuthProviderConnector;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.JsonWebTokenConnector;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.wrapper.UserInfoResponseWrapper;
import com.demo.bbq.infrastructure.authadapter.domain.exception.AuthAdapterException;
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

  private final AuthProviderConnector authProviderConnector;
  private final JsonWebTokenConnector jsonWebTokenConnector;

  public Single<TokenResponseWrapper> getToken(String username, String password) {
    return authProviderConnector.getToken(username, password);
  }

  public Completable logout(String refreshToken) {
    return authProviderConnector.logout(refreshToken)
        .doOnError(AuthAdapterException.ERROR0001::buildException);
  }

  public Single<TokenResponseWrapper> refreshToken(String refreshToken) {
    return authProviderConnector.refreshToken(refreshToken)
        .doOnError(AuthAdapterException.ERROR0002::buildException);
  }

  public Single<UserInfoResponseWrapper> getUserInfo(String authToken) {
    return authProviderConnector.getUserInfo(authToken);
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
      Jwk jwk = jsonWebTokenConnector.getJwk();
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
      algorithm.verify(jwt);
    } catch (Exception ex) {
      throw AuthAdapterException.ERROR0000.buildException(ex);
    }
  }

  private void verifyJwtExpiration(DecodedJWT jwt) {
    Date expiryDate = jwt.getExpiresAt();
    if (expiryDate.before(new Date())) {
      throw AuthAdapterException.ERROR0003.buildException();
    }
  }

}
