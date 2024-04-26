package com.demo.bbq.utils.properties.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTemplate {

  private String endpoint;
  private Map<String, String> headers;
}
