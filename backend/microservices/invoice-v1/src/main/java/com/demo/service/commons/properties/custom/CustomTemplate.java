package com.demo.service.commons.properties.custom;

import com.demo.service.commons.properties.custom.functional.FunctionalTemplate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomTemplate implements Serializable {

  private FunctionalTemplate functional;
}
