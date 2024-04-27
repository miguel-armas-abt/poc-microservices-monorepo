package com.demo.bbq.utils.errors.matcher;

import static com.demo.bbq.utils.errors.dto.ErrorDTO.CODE_DEFAULT;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.dto.ErrorType;
import com.demo.bbq.utils.errors.exceptions.AuthorizationException;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.Optional;

public abstract class ErrorMatcherUtil {

  public static <T extends Throwable> ErrorDTO build(ConfigurationBaseProperties properties, T exception) {
    ErrorDTO currentError = getError(exception);

    String errorCode = Optional.of(currentError.getCode()).orElseGet(() -> CODE_DEFAULT);

    String matchingMessage = getMatchMessage(properties, errorCode);
    if (matchingMessage == null)
      matchingMessage = getDefaultError(properties).getMessage();

    if (properties.isShowCustomMessages())
      currentError.setMessage(matchingMessage);

    return currentError;
  }

  private static <T extends Throwable> ErrorDTO getError(T exception) {
    ErrorDTO error = null;

    if (exception instanceof BusinessException businessException)
      error = businessException.getErrorDetail();

    if (exception instanceof SystemException systemException)
      error = systemException.getErrorDetail();

    if (exception instanceof AuthorizationException systemException)
      error = systemException.getErrorDetail();

    return error;
  }

  private static String getMatchMessage(ConfigurationBaseProperties properties, String errorCode) {
    return properties
        .getErrors()
        .get(errorCode);
  }

  public static ErrorDTO getDefaultError(ConfigurationBaseProperties properties) {
    return ErrorDTO
        .builder()
        .code(CODE_DEFAULT)
        .message(getMatchMessage(properties, CODE_DEFAULT))
        .type(ErrorType.SYSTEM)
        .build();
  }

}
