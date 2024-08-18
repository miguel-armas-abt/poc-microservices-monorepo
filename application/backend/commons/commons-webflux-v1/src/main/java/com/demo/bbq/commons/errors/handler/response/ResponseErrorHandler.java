package com.demo.bbq.commons.errors.handler.response;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.toolkit.serialize.ByteSerializer;
import com.demo.bbq.commons.tracing.logging.ErrorLoggerUtil;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.net.ConnectException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ResponseErrorHandler {

  private final ByteSerializer byteSerializer;

  public Mono<Void> handleException(ConfigurationBaseProperties properties, Throwable ex, ServerWebExchange exchange) {
    ErrorLoggerUtil.generateTrace(ex, exchange);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if (ex instanceof WebClientRequestException || ex instanceof ConnectException)
      httpStatus = HttpStatus.REQUEST_TIMEOUT;

    if( ex instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      httpStatus = HttpStatus.valueOf(externalServiceException.getHttpStatusCode().value());
    }

    if( ex instanceof BusinessException businessException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if( ex instanceof SystemException systemException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, systemException);
    }

    if( ex instanceof AuthorizationException authException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return buildResponse(error, httpStatus , exchange);
  }

  private Mono<Void> buildResponse(ErrorDTO error, HttpStatus httpStatus, ServerWebExchange exchange) {
    byte[] errorDetailByte = byteSerializer.toBytes(error, error.getMessage());

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    DataBuffer buffer = response.bufferFactory().wrap(errorDetailByte);
    return response.writeWith(Mono.just(buffer));
  }
}
