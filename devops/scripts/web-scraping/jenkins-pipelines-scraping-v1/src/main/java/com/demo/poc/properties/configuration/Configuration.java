package com.demo.poc.properties.configuration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

  private String driverPath;
  private long waitingTimeMillis;
  private Login login;
  private Pipeline pipeline;
}
