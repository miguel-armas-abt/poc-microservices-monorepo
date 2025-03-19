package com.demo.bbq.commons.properties.base.restclient;

import com.demo.bbq.commons.tracing.enums.TraceParamType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HeaderTemplate {

  private Map<String, String> provided;
  private Map<String, TraceParamType> generated;
  private Map<String, String> forwarded;

}
