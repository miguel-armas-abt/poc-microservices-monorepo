package com.demo.bbq.rest;

import com.demo.bbq.application.service.AuthenticationService;
import com.demo.bbq.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.repository.authprovider.wrapper.UserInfoResponseWrapper;
import com.demo.bbq.repository.authprovider.JsonWebTokenConnector;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletResponse;
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
  public Single<TokenResponseWrapper> login(HttpServletResponse servletResponse,
                                            @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
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
  public Single<TokenResponseWrapper> refresh(HttpServletResponse servletResponse,
                                              @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    return authenticationService.refreshToken(refreshToken)
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @GetMapping("/valid")
  public Single<UserInfoResponseWrapper> valid(@RequestHeader("Authorization") String authToken) {
    return authenticationService.getUserInfo(authToken);
  }

  @GetMapping("/roles")
  public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authToken) {
      return ResponseEntity.ok(authenticationService.getRoles(authToken));
  }
}
