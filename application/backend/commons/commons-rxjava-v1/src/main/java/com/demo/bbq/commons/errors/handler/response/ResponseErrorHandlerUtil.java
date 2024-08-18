package com.demo.bbq.commons.errors.handler.response;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.errors.handler.external.ExternalErrorHandlerUtil;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.net.ConnectException;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import retrofit2.HttpException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseErrorHandlerUtil {

  public static ResponseEntity<ErrorDTO> handleException(ConfigurationBaseProperties properties,
                                                         List<RestClientErrorStrategy> errorServices,
                                                         Throwable ex,
                                                         WebRequest request) {
    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if(ex instanceof HttpException httpException) {
      Pair<ErrorDTO, HttpStatus> pairError = ExternalErrorHandlerUtil.build(httpException.response(), errorServices, properties);
      httpStatus = pairError.getRight();
      error = pairError.getLeft();
    }

    if(ex instanceof ConnectException) {
      httpStatus = HttpStatus.REQUEST_TIMEOUT;
    }

    if(ex instanceof BusinessException businessException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if(ex instanceof SystemException systemException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, systemException);
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if(ex instanceof AuthorizationException authException) {
      error = ResponseErrorHandlerBase.toErrorDTO(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return new ResponseEntity<>(error, httpStatus);
  }

}
