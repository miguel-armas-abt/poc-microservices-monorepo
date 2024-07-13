package com.demo.bbq.commons.properties.dto.restclient;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RestClientError {

  private String customCode;
  private String message;
  private Integer httpCode;
}