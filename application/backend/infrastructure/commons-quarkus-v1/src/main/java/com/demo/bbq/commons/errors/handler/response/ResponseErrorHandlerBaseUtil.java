package com.demo.bbq.commons.errors.handler.response;

import static com.demo.bbq.commons.errors.dto.ErrorDTO.CODE_DEFAULT;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import java.util.Optional;

public class ResponseErrorHandlerBaseUtil {

  public static <T extends Throwable> ErrorDTO build(ConfigurationBaseProperties properties, T exception) {
    ErrorDTO currentError = getError(exception);
    String defaultMessage = ErrorDTO.getDefaultError(properties).getMessage();
    String errorCode = Optional.of(currentError.getCode()).orElseGet(() -> CODE_DEFAULT);
    String matchingMessage = ErrorDTO.getMatchMessage(properties, errorCode);
    boolean showCustomMessages = properties.errorMessages().get().enabled();

    String selectedMessage = selectMessage(showCustomMessages, defaultMessage, currentError.getMessage(), matchingMessage);
    currentError.setMessage(selectedMessage);
    return currentError;
  }

  private static String selectMessage(boolean showCustomMessages, String defaultMessage,
                                      String currentMessage, String matchingMessage) {
    return showCustomMessages
        ? Optional.ofNullable(matchingMessage).orElse(defaultMessage)
        : Optional.ofNullable(currentMessage).orElse(defaultMessage);
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
}
