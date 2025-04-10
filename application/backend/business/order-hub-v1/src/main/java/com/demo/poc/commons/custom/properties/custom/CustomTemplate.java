package com.demo.poc.commons.custom.properties.custom;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class CustomTemplate implements Serializable {

  private Map<String, Class<?>> selectorClass;
}
