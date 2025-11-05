package com.demo.service.entrypoint.auth.params.user;

import com.demo.commons.validations.headers.DefaultHeaders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoHeader extends DefaultHeaders {

  private String authorization;
}
