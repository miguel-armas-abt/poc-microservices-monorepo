package com.demo.bbq.entrypoint.auth.rest;

import com.demo.bbq.entrypoint.auth.service.AuthenticationService;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
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
@RequestMapping(value = "/bbq/infrastructure/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthAdapterRestService {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public Single<TokenResponseWrapper> login(HttpServletResponse servletResponse,
                                            @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
    return authenticationService.getToken(username, password)
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @PostMapping("/logout")
  public Completable logout(HttpServletResponse servletResponse,
                            @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {
    return authenticationService.logout(refreshToken)
        .doOnComplete(() -> servletResponse.setStatus(201))
        .andThen(Completable.complete());
  }

  @PostMapping(value = "/refresh")
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
