package com.demo.bbq.properties.configuration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

  private String driverPath;
  private long waitingTimeMillis;
  private Login login;
  private Realm realm;
  private Role role;
  private Client client;

}
