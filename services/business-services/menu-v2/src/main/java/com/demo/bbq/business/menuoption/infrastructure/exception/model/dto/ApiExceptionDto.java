package com.demo.bbq.business.menuoption.infrastructure.exception.model.dto;

import com.demo.bbq.business.menuoption.infrastructure.exception.constant.ApiExceptionConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

/**
 * <br/>Exception DTO to all application.
 *
 * type(Unique uri that identifies that categorizes the error, example: /errors/business-rules)
 * message(A brief, human-readable message about the error, example: The category is not defined)
 * errorCode(Unique error code that includes the error type followed by the specific error, example: 01.0001)
 * <br/>
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Builder
public class ApiExceptionDto implements Serializable {

  private final String type;

  private final String message;

  @Pattern(regexp = ApiExceptionConstant.ERROR_CODE_REGEX, message = "Invalid error code")
  private final String errorCode;
}
