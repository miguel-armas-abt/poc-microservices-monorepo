package com.demo.poc.entrypoint.auth.params.roles;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class RolesHeaderMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    RolesHeader headers = new RolesHeader();
    headers.setChannelId(params.get("channel-id"));
    headers.setTraceId(params.get("trace-id"));
    headers.setAuthorization(params.get("Authorization"));
    return headers;
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return RolesHeaderMapper.class.isAssignableFrom(paramClass);
  }
}
