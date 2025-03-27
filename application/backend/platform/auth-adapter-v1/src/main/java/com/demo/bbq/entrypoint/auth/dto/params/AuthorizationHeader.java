package com.demo.bbq.entrypoint.auth.dto.params;

import com.demo.bbq.commons.core.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.core.validations.utils.ParamName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationHeader extends DefaultHeaders {

  @ParamName("Authorization")
  private String authorization;
}
