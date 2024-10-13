package com.demo.bbq.commons.properties;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authentication implements Serializable {

  private boolean enableAuth;
  private String expectedRoles;
}
