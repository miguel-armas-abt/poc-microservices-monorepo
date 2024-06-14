package com.demo.bbq.commons.properties.dto;

import java.util.Map;

public interface ErrorMessage {

  boolean enabled();

  Map<String, String> messages();
}