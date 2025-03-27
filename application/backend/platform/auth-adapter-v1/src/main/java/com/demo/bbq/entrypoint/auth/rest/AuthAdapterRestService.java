package com.demo.bbq.entrypoint.auth.rest;

import com.demo.bbq.commons.core.restclient.utils.HttpHeadersFiller;
import com.demo.bbq.commons.core.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.core.validations.headers.HeaderValidator;
import com.demo.bbq.entrypoint.auth.dto.params.AuthorizationHeader;
import com.demo.bbq.entrypoint.auth.service.AuthenticationService;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
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
  private final HeaderValidator headerValidator;

  @PostMapping("/token")
  public Single<TokenResponseWrapper> login(HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse,
                                            @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {

    return Single.just(HttpHeadersFiller.extractHeadersAsMap(servletRequest))
        .doOnSuccess(headers -> headerValidator.validate(headers, DefaultHeaders.class))
        .flatMap(headers -> authenticationService.getToken(headers, username, password))
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @PostMapping("/logout")
  public Completable logout(HttpServletRequest servletRequest,
                            HttpServletResponse servletResponse,
                            @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {

    return Single.just(HttpHeadersFiller.extractHeadersAsMap(servletRequest))
        .doOnSuccess(headers -> headerValidator.validate(headers, DefaultHeaders.class))
        .flatMapCompletable(headers -> authenticationService.logout(headers, refreshToken))
        .doOnComplete(() -> servletResponse.setStatus(201))
        .andThen(Completable.complete());
  }

  @PostMapping(value = "/refresh")
  public Single<TokenResponseWrapper> refresh(HttpServletRequest servletRequest,
                                              HttpServletResponse servletResponse,
                                              @RequestParam(value = "refresh_token", name = "refresh_token") String refreshToken) {

    return Single.just(HttpHeadersFiller.extractHeadersAsMap(servletRequest))
        .doOnSuccess(headers -> headerValidator.validate(headers, DefaultHeaders.class))
        .flatMap(headers -> authenticationService.refreshToken(headers, refreshToken))
        .doOnSuccess(token -> servletResponse.setStatus(201));
  }

  @GetMapping("/user-info")
  public Single<UserInfoResponseWrapper> getUserInfo(HttpServletRequest servletRequest) {
    return Single.just(HttpHeadersFiller.extractHeadersAsMap(servletRequest))
        .doOnSuccess(headers -> headerValidator.validate(headers, AuthorizationHeader.class))
        .flatMap(authenticationService::getUserInfo);
  }

  @GetMapping("/roles")
  public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String authToken) {
      return ResponseEntity.ok(authenticationService.getRoles(authToken));
  }
}
