package com.demo.poc.entrypoint.auth.params.login;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam implements Serializable {

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;
}
