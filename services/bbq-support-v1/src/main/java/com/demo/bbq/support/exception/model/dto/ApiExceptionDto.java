package com.demo.bbq.support.exception.model.dto;

import com.demo.bbq.support.exception.constant.ApiExceptionConstant;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Pattern;

import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * <br/>Clase DTO que define el modelo de objeto para transmitir información de la excepción personalizada.
 *
 * type(notes: The unique uri identifier that categorizes the error, example: /errors/authentication)
 * message(notes: A brief, human-readable message about the error, example: The user does not have autorization)
 * errorCode(notes: The unique error code, example: 001.01.0001)
 * details(notes: A human-readable explanation specific to this occurrence of the problem)
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

  private final List<ApiExceptionDetail> details;
}
