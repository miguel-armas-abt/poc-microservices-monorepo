package com.demo.bbq.infrastructure.apigateway.infrastructure.exception;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDTO;
import com.google.gson.Gson;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class HandleErrorUtil {
  private HandleErrorUtil() {}

  public static Mono<Void> handleError(ServerWebExchange exchange, Throwable throwable) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    ApiExceptionDTO errorResponse;
    if (throwable instanceof ApiException) {
      ApiException apiException = (ApiException) throwable;
      httpStatus = apiException.getHttpStatus();
      errorResponse = fromApiExceptionToDTO.apply(apiException);
    } else {
      errorResponse = fromThrowableToDTO.apply(throwable);
    }
    setHeaders.accept(exchange, httpStatus);
    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap( new Gson().toJson(errorResponse).getBytes())))
        .doOnTerminate(() -> exchange.getResponse().setComplete());
  }

  private static final Function<ApiException, ApiExceptionDTO> fromApiExceptionToDTO = exception ->
      ApiExceptionDTO.builder()
          .type(exception.getType())
          .message(exception.getMessage())
          .errorCode(exception.getErrorCode())
          .details(exception.getDetails())
          .build();

  private static final Function<Throwable, ApiExceptionDTO> fromThrowableToDTO = exception ->
      ApiExceptionDTO.builder()
          .message(exception.getMessage())
          .build();

  private static final BiConsumer<ServerWebExchange, HttpStatus> setHeaders = (serverWebExchange, httpStatus) -> {
    serverWebExchange.getResponse().setStatusCode(httpStatus);
    serverWebExchange.getResponse().getHeaders().add("Content-Type", "application/json");
    serverWebExchange.getResponse().getHeaders().add("charset", "UTF-8");
  };

}
