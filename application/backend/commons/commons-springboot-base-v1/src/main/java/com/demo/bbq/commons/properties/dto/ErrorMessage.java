package com.demo.bbq.commons.properties.dto;

import java.util.Map;
import lombok.*;

@Getter
@Setter
public class ErrorMessage {

  private boolean enabled;

  private Map<String, String> messages;
}