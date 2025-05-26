package com.demo.poc.entrypoint.auth.params.refresh;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    return RefreshTokenParam.builder()
        .refreshToken(params.get("refresh_token"))
        .build();
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return RefreshTokenParam.class.isAssignableFrom(paramClass);
  }
}
