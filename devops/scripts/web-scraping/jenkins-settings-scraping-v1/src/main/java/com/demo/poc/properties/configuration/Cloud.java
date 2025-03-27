package com.demo.poc.properties.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cloud {

  private String name;
  private String certificate;
  private String localServerUrl;
  private String forwardedServerUrl;
}
