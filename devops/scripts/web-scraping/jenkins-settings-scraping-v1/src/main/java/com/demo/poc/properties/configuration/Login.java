package com.demo.poc.properties.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {

  private String uri;
  private String unlockPassword;
  private String username;
  private String password;
  private String email;
}
