package com.demo.bbq.commons.properties.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RestClientError {

  private String code;
  private String message;
  private int httpCode;
}