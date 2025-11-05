package com.demo.service.entrypoint.search.params;

import java.util.HashMap;
import java.util.Map;

import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class DocumentNumberParamMapper implements ParamMapper<DocumentNumberParam> {

  private static final String DOCUMENT_NUMBER = "documentNumber";
  private static final String DOCUMENT_TYPE = "documentType";

  @Override
  public Map.Entry<DocumentNumberParam, Map<String, String>> map(Map<String, String> params) {
    DocumentNumberParam documentNumberParam = DocumentNumberParam.builder()
        .documentNumber(params.get(DOCUMENT_NUMBER))
        .documentType(params.get(DOCUMENT_TYPE))
        .build();

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put(DOCUMENT_NUMBER, documentNumberParam.getDocumentNumber());
    paramMap.put(DOCUMENT_TYPE, documentNumberParam.getDocumentType());

    return Map.entry(documentNumberParam, paramMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return DocumentNumberParam.class.isAssignableFrom(paramClass);
  }
}
