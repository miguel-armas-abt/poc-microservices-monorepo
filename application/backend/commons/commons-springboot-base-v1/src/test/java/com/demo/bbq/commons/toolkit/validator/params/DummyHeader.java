package com.demo.bbq.commons.toolkit.validator.params;

import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.utils.ParamName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DummyHeader extends DefaultHeaders {

  @ParamName("Authorization")
  private String authorization;

  @ParamName("Content-Type")
  private String contentType;

  private String accept;
}