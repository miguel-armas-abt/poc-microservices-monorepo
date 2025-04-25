package com.demo.poc.entrypoint.auth.params.login;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class LoginParamMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    return LoginParam.builder()
        .username(params.get("username"))
        .password(params.get("password"))
        .build();
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return LoginParam.class.isAssignableFrom(paramClass);
  }
}
