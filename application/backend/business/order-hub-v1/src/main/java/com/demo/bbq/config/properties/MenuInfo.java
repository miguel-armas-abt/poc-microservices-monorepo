package com.demo.bbq.config.properties;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuInfo implements Serializable {

  private Class<?> selectorClass;
}
