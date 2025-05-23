package com.demo.poc.entrypoint.auth.params.roles;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesHeader extends DefaultHeaders {

  @NotEmpty
  private String authorization;
}
