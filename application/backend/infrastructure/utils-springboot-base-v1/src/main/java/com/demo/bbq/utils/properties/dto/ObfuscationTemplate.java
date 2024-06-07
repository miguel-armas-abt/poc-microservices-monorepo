package com.demo.bbq.utils.properties.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObfuscationTemplate {

  private Set<String> bodyFields;

  private Set<String> headers;
}