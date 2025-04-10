package com.demo.poc.commons.core.errors.selector;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.properties.ProjectType;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.demo.poc.commons.core.errors.dto.ErrorDto.CODE_DEFAULT;

@RequiredArgsConstructor
public class ResponseErrorSelector {

  private final ConfigurationBaseProperties properties;

  public <T extends Throwable> ErrorDto toErrorDTO(T exception) {
    ErrorDto error = extractError(exception);
    String selectedCode = selectCustomCode(error);
    error.setCode(selectedCode);

    String selectedMessage = selectMessage(error);
    error.setMessage(selectedMessage);

    return error;
  }

  private String selectCustomCode(ErrorDto error) {
    ProjectType projectType = selectProjectType();

    return ProjectType.BFF.equals(projectType)
        ? CODE_DEFAULT
        : Optional.ofNullable(error)
        .map(ErrorDto::getCode)
        .orElseGet(() -> CODE_DEFAULT);
  }

  private String selectMessage(ErrorDto error) {
    String defaultMessage = ErrorDto.getDefaultError(properties).getMessage();
    ProjectType projectType = selectProjectType();

    if(ProjectType.BFF.equals(projectType))
      return defaultMessage;

    return Optional.ofNullable(ErrorDto.getMatchMessage(properties, error.getCode()))
        .orElseGet(() -> Optional.ofNullable(error.getMessage()).orElse(defaultMessage));
  }

  private ProjectType selectProjectType() {
    return Optional.ofNullable(properties.getProjectType()).orElseGet(() -> ProjectType.MS);
  }

  private static <T extends Throwable> ErrorDto extractError(T exception) {
    ErrorDto error = null;

    if (exception instanceof GenericException genericException)
      error = genericException.getErrorDetail();

    return error;
  }
}
