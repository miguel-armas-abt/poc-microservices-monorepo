package com.demo.service.entrypoint.auth.params.user;

import java.util.Map;
import java.util.TreeMap;

import com.demo.commons.tracing.enums.ForwardedParam;
import com.demo.commons.tracing.enums.TraceParam;
import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class UserInfoHeaderMapper implements ParamMapper<UserInfoHeader> {

  private static final String AUTHORIZATION = "Authorization";

  @Override
  public Map.Entry<UserInfoHeader, Map<String, String>> map(Map<String, String> params) {
    UserInfoHeader userInfoHeaders = new UserInfoHeader();
    userInfoHeaders.setChannelId(params.get(ForwardedParam.CHANNEL_ID.getKey()));
    userInfoHeaders.setTraceParent(params.get(TraceParam.TRACE_PARENT.getKey()));
    userInfoHeaders.setAuthorization(params.get("Authorization"));

    Map<String, String> userInfoHeadersMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    userInfoHeadersMap.put(ForwardedParam.CHANNEL_ID.getKey(), userInfoHeaders.getChannelId());
    userInfoHeadersMap.put(TraceParam.TRACE_PARENT.getKey(), userInfoHeaders.getTraceParent());
    userInfoHeadersMap.put(AUTHORIZATION, userInfoHeaders.getAuthorization());

    return Map.entry(userInfoHeaders, userInfoHeadersMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return UserInfoHeader.class.isAssignableFrom(paramClass);
  }
}
