package com.demo.service.entrypoint.auth.params.refresh;

import java.util.HashMap;
import java.util.Map;

import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper implements ParamMapper<RefreshTokenParam> {

  private static final String REFRESH_TOKEN = "refresh_token";

  @Override
  public Map.Entry<RefreshTokenParam, Map<String, String>> map(Map<String, String> params) {
    RefreshTokenParam param = RefreshTokenParam.builder()
        .refreshToken(params.get("refresh_token"))
        .build();

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put(REFRESH_TOKEN, param.getRefreshToken());

    return Map.entry(param, paramMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return RefreshTokenParam.class.isAssignableFrom(paramClass);
  }
}
