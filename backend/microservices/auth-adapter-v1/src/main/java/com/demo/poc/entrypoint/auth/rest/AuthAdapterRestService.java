package com.demo.poc.entrypoint.auth.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthAdapterRestService {

  private static final String BASE_URI = "/poc/platform/auth-adapter/v1/auth/";

  @Bean(name = "auth-adapter")
  public RouterFunction<ServerResponse> build(AuthAdapterHandler handler) {
    return nest(
        path(BASE_URI),
        route()
            .POST("token", accept(APPLICATION_JSON) , handler::getToken)
            .POST("logout", accept(APPLICATION_JSON) , handler::logout)
            .POST("refresh", accept(APPLICATION_JSON) , handler::refresh)
            .GET("user-info", accept(APPLICATION_JSON) , handler::getUserInfo)
            .GET("roles", accept(APPLICATION_JSON) , handler::getRoles)
            .build()
    );
  }
}
