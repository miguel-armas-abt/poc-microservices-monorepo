package com.demo.poc.entrypoint.auth.params.user;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoHeader extends DefaultHeaders {

  private String authorization;
}
