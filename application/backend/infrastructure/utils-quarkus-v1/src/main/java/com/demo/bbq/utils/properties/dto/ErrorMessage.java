package com.demo.bbq.utils.properties.dto;

import java.util.Map;

public interface ErrorMessage {

  boolean enabled();

  Map<String, String> messages();
}