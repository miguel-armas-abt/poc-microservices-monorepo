package com.demo.poc.entrypoint.auth.rest;

import java.util.Map;

import com.demo.poc.commons.core.restserver.utils.RestServerUtils;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.auth.params.roles.RolesHeader;
import com.demo.poc.entrypoint.auth.params.user.UserInfoHeader;
import com.demo.poc.entrypoint.auth.params.login.LoginParam;
import com.demo.poc.entrypoint.auth.params.refresh.RefreshTokenParam;
import com.demo.poc.entrypoint.auth.service.AuthenticationService;
import com.demo.poc.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.poc.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/poc/infrastructure/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthAdapterRestService {

  private final AuthenticationService authenticationService;
  private final ParamValidator paramValidator;

  @PostMapping("/token")
  public Single<TokenResponseWrapper> login(HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse) {

    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(servletRequest);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .flatMap(defaultHeaders -> paramValidator.validateAndGet(RestServerUtils.extractQueryParamsAsMap(servletRequest), LoginParam.class)
            .flatMap(loginParam -> authenticationService.getToken(headers, loginParam.getUsername(), loginParam.getPassword())))
        .doOnSuccess(token -> servletResponse.setStatus(200));
  }

  @PostMapping("/logout")
  public Completable logout(HttpServletRequest servletRequest,
                            HttpServletResponse servletResponse) {

    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(servletRequest);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .flatMapCompletable(defaultHeaders -> paramValidator.validateAndGet(RestServerUtils.extractQueryParamsAsMap(servletRequest), RefreshTokenParam.class)
            .flatMapCompletable(refreshTokenParam -> authenticationService.logout(headers, refreshTokenParam.getRefreshToken())))
        .doOnComplete(() -> servletResponse.setStatus(200))
        .andThen(Completable.complete());
  }

  @PostMapping(value = "/refresh")
  public Single<TokenResponseWrapper> refresh(HttpServletRequest servletRequest,
                                              HttpServletResponse servletResponse) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(servletRequest);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .flatMap(defaultHeaders -> paramValidator.validateAndGet(RestServerUtils.extractQueryParamsAsMap(servletRequest), RefreshTokenParam.class)
            .flatMap(refreshTokenParam -> authenticationService.refreshToken(headers, refreshTokenParam.getRefreshToken())))
        .doOnSuccess(token -> servletResponse.setStatus(200));
  }

  @GetMapping("/user-info")
  public Single<UserInfoResponseWrapper> getUserInfo(HttpServletRequest servletRequest,
                                                     HttpServletResponse servletResponse) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(servletRequest);

    return paramValidator.validateAndGet(headers, UserInfoHeader.class)
        .flatMap(userInfoHeader -> authenticationService.getUserInfo(headers))
        .doOnSuccess(token -> servletResponse.setStatus(200));
  }

  @GetMapping("/roles")
  public Single<Map<String, Integer>> getRoles(HttpServletRequest servletRequest,
                                               HttpServletResponse servletResponse) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(servletRequest);

    return paramValidator.validateAndGet(headers, RolesHeader.class)
        .flatMap(rolesHeader -> Single.just(authenticationService.getRoles(rolesHeader.getAuthorization())))
        .doOnSuccess(token -> servletResponse.setStatus(200));
  }
}
