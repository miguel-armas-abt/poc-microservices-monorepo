package com.demo.bbq.support.exception.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * title(A human-readable explanation specific to this occurrence of the problem)
 * component(The component where the error is coming from)
 */
@Getter
@Data
@Builder
@AllArgsConstructor
public class ApiExceptionDetail implements Serializable {

  private String title;
  private String component;

}
