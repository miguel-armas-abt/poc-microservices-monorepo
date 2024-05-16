package com.demo.bbq.utils.properties.dto;

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