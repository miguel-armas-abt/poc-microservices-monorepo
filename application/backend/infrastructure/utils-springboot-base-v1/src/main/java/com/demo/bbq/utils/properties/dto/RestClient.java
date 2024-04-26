package com.demo.bbq.utils.properties.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestClient {

  private RequestTemplate request;
  private Map<String, String> variables;
  private Map<String, String> errors;

}
