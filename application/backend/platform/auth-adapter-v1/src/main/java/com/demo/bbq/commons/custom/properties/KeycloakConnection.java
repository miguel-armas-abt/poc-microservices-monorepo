package com.demo.bbq.commons.custom.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KeycloakConnection {
  private String jwkEndpoint;
  private String certId;
}
