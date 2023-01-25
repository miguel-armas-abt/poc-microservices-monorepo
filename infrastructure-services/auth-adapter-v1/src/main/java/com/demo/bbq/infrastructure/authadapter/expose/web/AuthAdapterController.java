package com.demo.bbq.infrastructure.authadapter.expose.web;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.bbq.infrastructure.authadapter.service.JsonWebTokenService;
import com.demo.bbq.infrastructure.authadapter.service.OauthProviderService;
import com.demo.bbq.infrastructure.authadapter.util.exception.ExceptionCatalog;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bbq/infrastructure/v1/auth")
public class AuthAdapterController {

  private final OauthProviderService oauthProviderService;
  private final JsonWebTokenService jsonWebTokenService;

  @GetMapping("/roles")
  public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authHeader) {
    try {
      DecodedJWT jwt = JWT.decode(authHeader.replace("Bearer", "").trim());

      //check JWT is valid
      Jwk jwk = jsonWebTokenService.getJwk();
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
      algorithm.verify(jwt);

      //check JWT role is correct
      List<String> roles = ((List) jwt.getClaim("realm_access").asMap().get("roles"));

      //check JWT is still active
      Date expiryDate = jwt.getExpiresAt();
      if (expiryDate.before(new Date())) {
        throw new Exception("token is expired");
      }
      //all validation passed
      HashMap HashMap = new HashMap();
      for (String str : roles) {
        HashMap.put(str, str.length());
      }
      return ResponseEntity.ok(HashMap);

    } catch (Exception e) {
      log.error("exception : {} ", e.getMessage());
      throw ExceptionCatalog.ERROR1000.buildException();
    }
  }

  @GetMapping("/valid")
  public ResponseEntity<?> valid(@RequestHeader("Authorization") String authHeader) {
    try {
      oauthProviderService.checkValidity(authHeader);
      return ResponseEntity.ok(new HashMap() {{
        put("is_valid", "true");
      }});
    } catch (Exception e) {
      log.error("token is not valid, exception : {} ", e.getMessage());
      throw ExceptionCatalog.ERROR1001.buildException();
    }
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> login(String username, String password) {
    String login = oauthProviderService.login(username, password);
    return ResponseEntity.ok(login);
  }

  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> logout(@RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    try {
      oauthProviderService.logout(refreshToken);
      return ResponseEntity.ok(new HashMap() {{
        put("logout", "true");
      }});
    } catch (Exception e) {
      log.error("unable to logout, exception : {} ", e.getMessage());
      throw ExceptionCatalog.ERROR1002.buildException();
    }
  }

  @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> refresh(@RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    try {
      return ResponseEntity.ok(oauthProviderService.refresh(refreshToken));
    } catch (Exception e) {
      log.error("unable to refresh, exception : {} ", e.getMessage());
      throw ExceptionCatalog.ERROR1003.buildException();
    }
  }

}
