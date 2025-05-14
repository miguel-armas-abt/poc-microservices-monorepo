package com.demo.poc.entrypoint.auth.params.roles;

import java.util.Map;

import com.demo.poc.commons.core.tracing.enums.ForwardedParam;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class RolesHeaderMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    RolesHeader headers = new RolesHeader();
    headers.setChannelId(params.get(ForwardedParam.CHANNEL_ID.getKey()));
    headers.setTraceParent(params.get(TraceParam.TRACE_PARENT.getKey()));
    headers.setAuthorization(params.get("Authorization"));
    return headers;
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return RolesHeader.class.isAssignableFrom(paramClass);
  }
}
