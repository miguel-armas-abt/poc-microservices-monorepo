package com.demo.bbq.infrastructure.authadapter.infrastructure.resource.rest;

import com.demo.bbq.infrastructure.authadapter.application.service.AuthenticationService;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.JsonWebTokenConnector;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bbq/infrastructure/v1/auth")
public class AuthAdapterRestService {

  private final JsonWebTokenConnector jsonWebTokenConnector;

  private final AuthenticationService authenticationService;

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Single<TokenResponse> login(HttpServletResponse servletResponse,
                                     String username, String password) {
    return authenticationService.getToken(username, password)
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public Completable logout(HttpServletResponse servletResponse,
                            @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    return authenticationService.logout(refreshToken)
        .doOnComplete(() -> servletResponse.setStatus(201))
        .andThen(Completable.complete());
  }

  @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  public Single<TokenResponse> refresh(HttpServletResponse servletResponse,
                                       @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    return authenticationService.refreshToken(refreshToken)
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @GetMapping("/valid")
  public Single<UserInfoResponse> valid(@RequestHeader("Authorization") String authToken) {
    return authenticationService.getUserInfo(authToken);
  }

  @GetMapping("/roles")
  public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authToken) {
      return ResponseEntity.ok(authenticationService.getRoles(authToken));
  }
}
