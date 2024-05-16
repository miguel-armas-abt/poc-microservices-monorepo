package com.demo.bbq.utils.properties.dto;

import java.util.Map;
import lombok.*;

@Getter
@Setter
public class ErrorMessage {

  private boolean enabled;

  private Map<String, String> messages;
}