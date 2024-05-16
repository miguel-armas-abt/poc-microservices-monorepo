package com.demo.bbq.utils.errors.handler.response;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.net.ConnectException;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import retrofit2.HttpException;

public class ResponseErrorHandlerUtil {

  private ResponseErrorHandlerUtil() {}

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
      error = ResponseErrorHandlerBaseUtil.build(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if(ex instanceof SystemException systemException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, systemException);
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if(ex instanceof AuthorizationException authException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return new ResponseEntity<>(error, httpStatus);
  }

}
