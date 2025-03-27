package com.demo.poc.commons.core.properties.restclient;

import java.util.Map;

public interface HeaderTemplate {

  Map<String, String> provided();
  Map<String, String> generated();
  Map<String, String> forwarded();

}
