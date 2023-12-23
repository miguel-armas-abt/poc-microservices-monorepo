package com.demo.bbq.infrastructure.authadapter.application.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.AuthenticationProviderConnector;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.JsonWebTokenConnector;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import com.demo.bbq.infrastructure.authadapter.domain.exception.AuthAdapterException;
import io.reactivex.Completable;
import io.reactivex.Single;
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

  private final AuthenticationProviderConnector authenticationProviderConnector;
  private final JsonWebTokenConnector jsonWebTokenConnector;

  public Single<TokenResponse> getToken(String username, String password) {
    return authenticationProviderConnector.getToken(username, password);
  }

  public Completable logout(String refreshToken) {
    return authenticationProviderConnector.logout(refreshToken)
        .doOnError(AuthAdapterException.ERROR0001::buildException);
  }

  public Single<TokenResponse> refreshToken(String refreshToken) {
    return authenticationProviderConnector.refreshToken(refreshToken)
        .doOnError(AuthAdapterException.ERROR0002::buildException);
  }

  public Single<UserInfoResponse> getUserInfo(String authToken) {
    return authenticationProviderConnector.getUserInfo(authToken);
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
