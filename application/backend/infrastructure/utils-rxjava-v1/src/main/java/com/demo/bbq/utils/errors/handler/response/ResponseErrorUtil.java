package com.demo.bbq.utils.errors.handler.response;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorUtil;
import com.demo.bbq.utils.errors.handler.external.service.RestClientErrorService;
import com.demo.bbq.utils.errors.handler.matcher.ErrorMatcherUtil;
import com.demo.bbq.utils.errors.handler.serializer.ExceptionSerializerUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.net.ConnectException;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import retrofit2.HttpException;

public class ResponseErrorUtil {

  private ResponseErrorUtil() {}

  public static ResponseEntity<ErrorDTO> handleException(ConfigurationBaseProperties properties,
                                                         List<RestClientErrorService> errorServices,
                                                         Throwable ex,
                                                         WebRequest request) {
    ErrorDTO error = ErrorMatcherUtil.getDefaultError(properties);
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if(ex instanceof HttpException httpException) {
      Pair<ErrorDTO, HttpStatus> pairError = ExceptionSerializerUtil.matchExternalError(httpException.response(), errorServices, properties);
      httpStatus = pairError.getRight();
      error = pairError.getLeft();
    }

    if(ex instanceof ConnectException) {
      httpStatus = HttpStatus.REQUEST_TIMEOUT;
    }

    if(ex instanceof BusinessException businessException) {
      error = ErrorMatcherUtil.replaceWithMatchError(properties, businessException);
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    if(ex instanceof SystemException systemException) {
      error = ErrorMatcherUtil.replaceWithMatchError(properties, systemException);
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if(ex instanceof AuthorizationException authException) {
      error = ErrorMatcherUtil.replaceWithMatchError(properties, authException);
      httpStatus = HttpStatus.UNAUTHORIZED;
    }

    return new ResponseEntity<>(error, httpStatus);
  }

}
