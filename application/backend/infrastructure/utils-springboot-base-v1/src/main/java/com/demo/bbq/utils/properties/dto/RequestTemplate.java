package com.demo.bbq.utils.properties.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTemplate {

  private String endpoint;
  private HeaderTemplate headers;
}
