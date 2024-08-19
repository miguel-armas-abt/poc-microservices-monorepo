package com.demo.bbq.commons.toolkit.validator.headers;

import static com.demo.bbq.commons.toolkit.validator.utils.CaseInsensitiveUtil.*;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultHeadersMapper implements ParamMapper<DefaultHeaders> {

  @Override
  public DefaultHeaders mapParameters(Map<String, String> parametersMap) {
    parametersMap = toLowerCaseKeys(parametersMap);

    return DefaultHeaders.builder()
        .channelId(getInsensitiveCaseParam(parametersMap, "channel-id"))
        .traceId(getInsensitiveCaseParam(parametersMap, "trace-id"))
        .build();
  }

  @Override
  public boolean supports(Class<DefaultHeaders> paramsClass) {
    return paramsClass.isAssignableFrom(DefaultHeaders.class);
  }
}