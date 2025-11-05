package com.demo.service.entrypoint.auth.params.login;

import java.util.HashMap;
import java.util.Map;

import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class LoginParamMapper implements ParamMapper<LoginParam> {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  @Override
  public Map.Entry<LoginParam, Map<String, String>> map(Map<String, String> params) {
    LoginParam param = LoginParam.builder()
        .username(params.get("username"))
        .password(params.get("password"))
        .build();

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put(USERNAME, param.getUsername());
    paramMap.put(PASSWORD, param.getPassword());

    return Map.entry(param, paramMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return LoginParam.class.isAssignableFrom(paramClass);
  }
}
