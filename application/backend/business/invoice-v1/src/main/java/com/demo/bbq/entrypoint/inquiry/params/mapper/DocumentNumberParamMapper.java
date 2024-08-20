package com.demo.bbq.entrypoint.inquiry.params.mapper;

import static com.demo.bbq.entrypoint.inquiry.params.constants.ParamConstants.DOCUMENT_NUMBER_PARAM;
import static com.demo.bbq.entrypoint.inquiry.params.constants.ParamConstants.DOCUMENT_TYPE_PARAM;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.entrypoint.inquiry.params.pojo.DocumentNumberParam;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DocumentNumberParamMapper implements ParamMapper<DocumentNumberParam> {

  @Override
  public DocumentNumberParam mapParameters(Map<String, String> parametersMap) {
    return DocumentNumberParam.builder()
        .documentNumber(Optional.ofNullable(parametersMap.get(DOCUMENT_NUMBER_PARAM)).orElseGet(() -> null))
        .documentType(Optional.ofNullable(parametersMap.get(DOCUMENT_TYPE_PARAM)).orElseGet(() -> null))
        .build();
  }

  @Override
  public boolean supports(Class<DocumentNumberParam> paramsClass) {
    return paramsClass.isAssignableFrom(DocumentNumberParam.class);
  }
}
