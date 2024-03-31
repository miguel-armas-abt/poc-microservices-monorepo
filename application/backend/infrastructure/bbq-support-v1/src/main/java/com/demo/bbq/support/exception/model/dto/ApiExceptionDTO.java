package com.demo.bbq.support.exception.model.dto;

import com.demo.bbq.support.exception.model.ApiExceptionDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * <br/>Exception DTO to all application.
 *
 * type(Unique uri that identifies that categorizes the error, example: /errors/business-rules)
 * message(A brief, human-readable message about the error, example: The category is not defined)
 * errorCode(Unique error code that includes the error type followed by the specific error, example: 01.0001)
 * details(List of specific explanations for this occurrence of the problem)
 * <br/>
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Builder
public class ApiExceptionDTO implements Serializable {

  private final String type;

  private final String message;

  private final String errorCode;

  private final List<ApiExceptionDetail> details;
}
