package com.demo.bbq.commons.core.interceptor.error;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.dto.ErrorType;
import com.demo.bbq.commons.core.errors.enums.ErrorDictionary;
import com.demo.bbq.commons.core.errors.exceptions.GenericException;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.core.properties.ProjectType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.bbq.commons.core.errors.dto.ErrorDTO.CODE_DEFAULT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseErrorSelector {

  public static <T extends Throwable> ErrorDTO toErrorDTO(ConfigurationBaseProperties properties, T exception) {
    ErrorDTO error = extractError(exception);
    String selectedCode = selectCustomCode(properties, error);
    error.setCode(selectedCode);

    String selectedMessage = selectMessage(properties, error);
    error.setMessage(selectedMessage);

    return error;
  }

  private static String selectCustomCode(ConfigurationBaseProperties properties, ErrorDTO error) {
    ProjectType projectType = selectProjectType(properties);

    return ProjectType.BFF.equals(projectType)
        ? CODE_DEFAULT
        : Optional.ofNullable(error)
        .map(ErrorDTO::getCode)
        .orElseGet(() -> CODE_DEFAULT);
  }

  private static String selectMessage(ConfigurationBaseProperties properties, ErrorDTO error) {
    String defaultMessage = ErrorDTO.getDefaultError(properties).getMessage();
    ProjectType projectType = selectProjectType(properties);

    if (ProjectType.BFF.equals(projectType))
      return defaultMessage;

    return Optional.ofNullable(ErrorDTO.getMatchMessage(properties, error.getCode()))
        .orElseGet(() -> Optional.ofNullable(error.getMessage()).orElse(defaultMessage));
  }

  private static ProjectType selectProjectType(ConfigurationBaseProperties properties) {
    return Optional.ofNullable(properties.getProjectType()).orElseGet(() -> ProjectType.MS);
  }

  private static <T extends Throwable> ErrorDTO extractError(T exception) {
    ErrorDTO error = null;

    if (exception instanceof GenericException genericException)
      error = genericException.getErrorDetail();

    if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidExceptionD) {
      error = ErrorDTO.builder()
          .type(ErrorType.BUSINESS)
          .message(methodArgumentNotValidExceptionD.getBindingResult()
              .getFieldErrors()
              .stream()
              .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
              .collect(Collectors.joining(";")))
          .code(ErrorDictionary.INVALID_FIELD.getCode())
          .build();
    }

    return error;
  }
}
