package com.demo.bbq.support.exception.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * message(notes: A human-readable explanation specific to this occurrence of the problem)
 * errorCode(notes: The unique error code, example: 001.01.0001)
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Data
@Builder
@AllArgsConstructor
public class ApiExceptionDetail implements Serializable {

  private String message;
  private String errorCode;

}
