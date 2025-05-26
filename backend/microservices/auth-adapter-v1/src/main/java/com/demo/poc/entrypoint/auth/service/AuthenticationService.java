package com.demo.poc.entrypoint.auth.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.poc.commons.custom.exceptions.ExpiredTokenException;
import com.demo.poc.commons.custom.exceptions.InvalidJwtException;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.auth.repository.JsonKeyWrappingRepository;
import com.demo.poc.entrypoint.auth.repository.authtoken.*;
import com.demo.poc.entrypoint.auth.repository.authtoken.wrapper.TokenResponseWrapper;
import com.demo.poc.entrypoint.auth.repository.logout.LogoutRepository;
import com.demo.poc.entrypoint.auth.repository.userinfo.wrapper.UserInfoResponseWrapper;
import com.demo.poc.entrypoint.auth.repository.refreshtoken.RefreshTokenRepository;
import com.demo.poc.entrypoint.auth.repository.userinfo.UserInfoRepository;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final ApplicationProperties properties;
  private final AuthTokenRepository authTokenRepository;
  private final LogoutRepository logoutRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserInfoRepository userInfoRepository;
  private final JsonKeyWrappingRepository jsonKeyWrappingRepository;

  public Mono<TokenResponseWrapper> getToken(Map<String, String> currentHeaders, String username, String password) {
    return authTokenRepository.getToken(currentHeaders, username, password);
  }

  public Mono<Void> logout(Map<String, String> headers, String refreshToken) {
    return logoutRepository.logout(headers, refreshToken);
  }

  public Mono<TokenResponseWrapper> refreshToken(Map<String, String> headers, String refreshToken) {
    return refreshTokenRepository.refreshToken(headers, refreshToken);
  }

  public Mono<UserInfoResponseWrapper> getUserInfo(Map<String, String> headers) {
    return userInfoRepository.getUserInfo(headers);
  }

  public Map<String, Integer> getRoles(String authToken) {
      DecodedJWT jwt = JWT.decode(authToken.replace("Bearer", StringUtils.EMPTY).trim());

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
      throw new InvalidJwtException(ex.getMessage());
    }
  }

  private void verifyJwtExpiration(DecodedJWT jwt) {
    Date expiryDate = jwt.getExpiresAt();
    if (expiryDate.before(new Date())) {
      throw new ExpiredTokenException();
    }
  }

}
