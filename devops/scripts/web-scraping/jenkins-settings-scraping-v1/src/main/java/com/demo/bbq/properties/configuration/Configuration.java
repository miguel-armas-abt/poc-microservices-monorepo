package com.demo.bbq.properties.configuration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

  private String driverPath;
  private long waitingTimeMillis;
  private long waitingTimeAfterSuggestedPlugins;
  private long waitingTimeAfterRestart;
  private Login login;
  private K8s k8s;

}
