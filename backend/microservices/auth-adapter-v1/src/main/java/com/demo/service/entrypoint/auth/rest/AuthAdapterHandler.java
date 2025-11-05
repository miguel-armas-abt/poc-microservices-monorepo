package com.demo.service.entrypoint.auth.rest;

import java.util.Map;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.auth.params.roles.RolesHeader;
import com.demo.service.entrypoint.auth.params.user.UserInfoHeader;
import com.demo.service.entrypoint.auth.params.login.LoginParam;
import com.demo.service.entrypoint.auth.params.refresh.RefreshTokenParam;
import com.demo.service.entrypoint.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthAdapterHandler {

  private final AuthenticationService authenticationService;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> getToken(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateFormDataAndGet(serverRequest, LoginParam.class))
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          LoginParam formData = tuple.getT2().getKey();
          return authenticationService.getToken(headers, formData.getUsername(), formData.getPassword());
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }

  public Mono<ServerResponse> logout(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateFormDataAndGet(serverRequest, RefreshTokenParam.class))
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          RefreshTokenParam formData = tuple.getT2().getKey();
          return authenticationService.logout(headers, formData.getRefreshToken());
        })
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> refresh(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateFormDataAndGet(serverRequest, RefreshTokenParam.class))
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          RefreshTokenParam formData = tuple.getT2().getKey();
          return authenticationService.refreshToken(headers, formData.getRefreshToken());
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }

  public Mono<ServerResponse> getUserInfo(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    return paramValidator.validateAndGet(headers, UserInfoHeader.class)
        .flatMap(userInfoHeader -> authenticationService.getUserInfo(headers))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }

  public Mono<ServerResponse> getRoles(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, RolesHeader.class)
        .flatMap(rolesHeader -> {
          RolesHeader headers = rolesHeader.getKey();
          return Mono.just(authenticationService.getRoles(headers.getAuthorization()));
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
