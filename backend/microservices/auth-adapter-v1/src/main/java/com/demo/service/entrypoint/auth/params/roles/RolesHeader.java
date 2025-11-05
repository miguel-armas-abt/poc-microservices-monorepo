package com.demo.service.entrypoint.auth.params.roles;

import com.demo.commons.validations.headers.DefaultHeaders;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesHeader extends DefaultHeaders {

  @NotEmpty
  private String authorization;
}
