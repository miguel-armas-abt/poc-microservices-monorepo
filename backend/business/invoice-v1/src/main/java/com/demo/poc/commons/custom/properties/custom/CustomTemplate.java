package com.demo.poc.commons.custom.properties.custom;

import com.demo.poc.commons.custom.properties.custom.functional.FunctionalTemplate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomTemplate implements Serializable {

  private FunctionalTemplate functional;
}
