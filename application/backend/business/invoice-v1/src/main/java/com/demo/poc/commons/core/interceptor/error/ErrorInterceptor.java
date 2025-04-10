package com.demo.poc.commons.core.interceptor.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.exceptions.ExternalServiceException;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.serialization.ByteSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@RequiredArgsConstructor
public class ErrorInterceptor {

  private final ByteSerializer byteSerializer;
  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  public Mono<Void> handleException(ConfigurationBaseProperties properties, Throwable ex, ServerWebExchange exchange) {
    generateTrace(ex, exchange);

    ErrorDto error = ErrorDto.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if (ex instanceof WebClientRequestException || ex instanceof ConnectException)
      httpStatus = HttpStatus.REQUEST_TIMEOUT;

    if( ex instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      httpStatus = HttpStatus.valueOf(externalServiceException.getHttpStatusCode().value());
    }

    if( ex instanceof GenericException genericException) {
      error = ResponseErrorSelector.toErrorDTO(properties, genericException);
      httpStatus = genericException.getHttpStatus();
    }

    return buildResponse(error, httpStatus , exchange);
  }

  private Mono<Void> buildResponse(ErrorDto error, HttpStatus httpStatus, ServerWebExchange exchange) {
    byte[] errorDetailByte = byteSerializer.toBytes(error, error.getMessage());

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    DataBuffer buffer = response.bufferFactory().wrap(errorDetailByte);
    return response.writeWith(Mono.just(buffer));
  }

  public void generateTrace(Throwable ex, ServerWebExchange exchange) {
    if(properties.isLoggerPresent(LoggingType.ERROR))
      threadContextInjector.populateFromException(ex, exchange);
  }
}
