package com.demo.poc.entrypoint.auth.dto.params;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.utils.ParamName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationHeader extends DefaultHeaders {

  @ParamName("Authorization")
  private String authorization;
}
