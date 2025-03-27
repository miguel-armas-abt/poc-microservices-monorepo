package com.demo.poc.commons.properties.dto;

import java.util.Map;

public interface ErrorMessage {

  boolean enabled();

  Map<String, String> messages();
}