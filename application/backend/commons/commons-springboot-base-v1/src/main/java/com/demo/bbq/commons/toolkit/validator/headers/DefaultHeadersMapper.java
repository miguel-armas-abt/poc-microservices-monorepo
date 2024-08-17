package com.demo.bbq.commons.toolkit.validator.headers;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultHeadersMapper implements ParamMapper<DefaultHeaders> {

  @Override
  public DefaultHeaders mapParameters(Map<String, String> parametersMap) {
    return DefaultHeaders.builder()
        .channelId(parametersMap.get("channel-id"))
        .traceId(parametersMap.get("channel-id"))
        .build();
  }

  @Override
  public boolean supports(Class<DefaultHeaders> paramsClass) {
    return paramsClass.isAssignableFrom(DefaultHeaders.class);
  }
}