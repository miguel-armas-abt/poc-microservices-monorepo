package com.demo.poc.properties.configuration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

  private OutputFile outputFile;
  private String driverPath;
  private long waitingTimeMillis;
  private Login login;
  private Realm realm;
  private Role role;
  private Client client;

}
