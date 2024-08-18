package com.demo.bbq.commons.properties.dto.restclient;

import com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderTemplate {

  private Map<String, String> provided;
  private Map<String, GeneratedParamType> generated;
  private Map<String, String> forwarded;

}
