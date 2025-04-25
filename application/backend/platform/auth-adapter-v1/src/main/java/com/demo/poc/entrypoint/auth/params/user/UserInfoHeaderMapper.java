package com.demo.poc.entrypoint.auth.params.user;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;
import com.demo.poc.entrypoint.auth.params.login.LoginParam;

import org.springframework.stereotype.Component;

@Component
public class UserInfoHeaderMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    UserInfoHeader headers = new UserInfoHeader();
    headers.setChannelId(params.get("channel-id"));
    headers.setTraceId(params.get("trace-id"));
    headers.setAuthorization(params.get("Authorization"));
    return headers;
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return UserInfoHeaderMapper.class.isAssignableFrom(paramClass);
  }
}
