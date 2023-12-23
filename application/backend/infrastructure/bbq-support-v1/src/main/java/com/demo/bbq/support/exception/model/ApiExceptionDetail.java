package com.demo.bbq.support.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Data
@Builder
@AllArgsConstructor
public class ApiExceptionDetail implements Serializable {

  private String message;
  private String errorCode;
}
