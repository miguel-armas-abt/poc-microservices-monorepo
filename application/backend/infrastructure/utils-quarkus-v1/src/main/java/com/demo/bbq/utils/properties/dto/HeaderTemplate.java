package com.demo.bbq.utils.properties.dto;

import java.util.Map;

public interface HeaderTemplate {

  Map<String, String> provided();
  Map<String, String> generated();
  Map<String, String> forwarded();

}
