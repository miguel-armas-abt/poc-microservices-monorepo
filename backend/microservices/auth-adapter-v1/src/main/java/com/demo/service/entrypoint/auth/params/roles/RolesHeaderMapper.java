package com.demo.service.entrypoint.auth.params.roles;

import java.util.Map;
import java.util.TreeMap;

import com.demo.commons.tracing.enums.ForwardedParam;
import com.demo.commons.tracing.enums.TraceParam;
import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class RolesHeaderMapper implements ParamMapper<RolesHeader> {

  private static final String AUTHORIZATION = "Authorization";

  @Override
  public Map.Entry<RolesHeader, Map<String, String>> map(Map<String, String> params) {
    RolesHeader rolesHeader = new RolesHeader();
    rolesHeader.setChannelId(params.get(ForwardedParam.CHANNEL_ID.getKey()));
    rolesHeader.setTraceParent(params.get(TraceParam.TRACE_PARENT.getKey()));
    rolesHeader.setAuthorization(params.get(AUTHORIZATION));

    Map<String, String> rolesHeadersMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    rolesHeadersMap.put(ForwardedParam.CHANNEL_ID.getKey(), rolesHeader.getChannelId());
    rolesHeadersMap.put(TraceParam.TRACE_PARENT.getKey(), rolesHeader.getTraceParent());
    rolesHeadersMap.put(AUTHORIZATION, rolesHeader.getAuthorization());

    return Map.entry(rolesHeader, rolesHeadersMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return RolesHeader.class.isAssignableFrom(paramClass);
  }
}
