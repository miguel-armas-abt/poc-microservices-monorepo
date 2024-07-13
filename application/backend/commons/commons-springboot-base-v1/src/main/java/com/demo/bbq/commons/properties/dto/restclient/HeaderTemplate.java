package com.demo.bbq.commons.properties.dto.restclient;

import java.util.Map;

import com.demo.bbq.commons.toolkit.restclient.headers.GeneratedHeaderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderTemplate {

  private Map<String, String> provided;
  private Map<String, GeneratedHeaderType> generated;
  private Map<String, String> forwarded;

}
