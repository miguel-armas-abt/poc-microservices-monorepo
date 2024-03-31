package com.demo.bbq.business.tableplacement.infrastructure.exception.handler;

import com.demo.bbq.support.exception.enums.ApiExceptionType;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiExceptionHandlerUtil {

  private final ObjectMapper objectMapper;

  public Mono<Void> buildResponse(Throwable throwable, ServerWebExchange exchange) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    if (throwable instanceof ApiException)
      httpStatus = ((ApiException) throwable).getHttpStatus();

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    response.getHeaders().setContentType(MediaType.APPLICATION_STREAM_JSON);

    byte[] exceptionResponse = serializeExceptionResponseToBytes(throwable);
    DataBuffer buffer = response.bufferFactory().wrap(exceptionResponse);
    return response.writeWith(Mono.just(buffer));
  }

  private byte[] serializeExceptionResponseToBytes(Throwable throwable) {
    ApiExceptionDTO exceptionResponse = (throwable instanceof ApiException)
        ? buildApiException.apply(throwable)
        : buildDefaultExceptionResponse.apply(throwable);
    try {
      return this.objectMapper.writeValueAsBytes(exceptionResponse);
    } catch (JsonProcessingException e) {
      return exceptionResponse.getMessage().getBytes(StandardCharsets.UTF_8);
    }
  }

  private static final Function<Throwable, ApiExceptionDTO> buildApiException = throwable -> {
    ApiException apiException = (ApiException) throwable;
    return ApiExceptionDTO.builder().type(apiException.getType()).message(apiException.getMessage()).errorCode(apiException.getErrorCode()).details(apiException.getDetails()).build();
  };

  private static final Function<Throwable, ApiExceptionDTO> buildDefaultExceptionResponse = throwable ->
      ApiExceptionDTO.builder()
          .message(throwable.getMessage())
          .type(ApiExceptionType.UNEXPECTED.getDescription())
          .build();
}