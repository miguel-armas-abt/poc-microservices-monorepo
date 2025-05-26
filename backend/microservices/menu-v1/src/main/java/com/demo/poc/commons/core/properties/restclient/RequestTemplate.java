package com.demo.poc.commons.core.properties.restclient;

import java.util.Map;

public interface RequestTemplate {

  String endpoint();
  HeaderTemplate headers();
  Map<String, String> formData();
}
