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
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<LoginParam> loginParamMono = RestServerUtils.extractFormDataAsMap(serverRequest)
        .flatMap(formData -> paramValidator.validateAndGet(formData, LoginParam.class));

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(loginParamMono)
        .flatMap(tuple -> authenticationService.getToken(headers, tuple.getT2().getUsername(), tuple.getT2().getPassword()))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }

  public Mono<ServerResponse> logout(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<RefreshTokenParam> refreshTokenParamMono = RestServerUtils.extractFormDataAsMap(serverRequest)
        .flatMap(formData -> paramValidator.validateAndGet(formData, RefreshTokenParam.class));

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(refreshTokenParamMono)
        .flatMap(tuple -> authenticationService.logout(headers, tuple.getT2().getRefreshToken()))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> refresh(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<RefreshTokenParam> refreshTokenParamMono = RestServerUtils.extractFormDataAsMap(serverRequest)
        .flatMap(formData -> paramValidator.validateAndGet(formData, RefreshTokenParam.class));

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(refreshTokenParamMono)
        .flatMap(tuple -> authenticationService.refreshToken(headers, tuple.getT2().getRefreshToken()))
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
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    return paramValidator.validateAndGet(headers, RolesHeader.class)
        .flatMap(rolesHeader -> Mono.just(authenticationService.getRoles(rolesHeader.getAuthorization())))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
