package com.demo.poc.entrypoint.auth.params.user;

import java.util.Map;

import com.demo.poc.commons.core.tracing.enums.ForwardedParam;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class UserInfoHeaderMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    UserInfoHeader headers = new UserInfoHeader();
    headers.setChannelId(params.get(ForwardedParam.CHANNEL_ID.getKey()));
    headers.setTraceParent(params.get(TraceParam.TRACE_PARENT.getKey()));
    headers.setAuthorization(params.get("Authorization"));
    return headers;
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return UserInfoHeader.class.isAssignableFrom(paramClass);
  }
}
