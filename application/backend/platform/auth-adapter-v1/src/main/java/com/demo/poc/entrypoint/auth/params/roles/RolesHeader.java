package com.demo.poc.entrypoint.auth.params.roles;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesHeader extends DefaultHeaders {

  private String authorization;
}
