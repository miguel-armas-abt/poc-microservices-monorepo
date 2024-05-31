package com.demo.bbq.utils.errors.handler.response;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.errors.serializer.ErrorSerializerUtil;
import com.demo.bbq.utils.tracing.logging.ErrorLoggerUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.net.ConnectException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ResponseErrorHandlerUtil {

  private ResponseErrorHandlerUtil() {}

  public static Mono<Void> handleException(ConfigurationBaseProperties properties, Throwable ex, ServerWebExchange exchange) {
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
      error = ResponseErrorHandlerBaseUtil.build(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if( ex instanceof SystemException systemException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, systemException);
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if( ex instanceof AuthorizationException authException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return buildResponse(error, httpStatus , exchange);
  }

  private static Mono<Void> buildResponse(ErrorDTO error, HttpStatus httpStatus, ServerWebExchange exchange) {
    byte[] errorDetailByte = ErrorSerializerUtil.serializeObject(error, error.getMessage());

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    DataBuffer buffer = response.bufferFactory().wrap(errorDetailByte);
    return response.writeWith(Mono.just(buffer));
  }
}
