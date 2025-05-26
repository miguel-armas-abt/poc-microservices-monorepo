package com.demo.poc.commons.core.validations.headers;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultHeaders implements Serializable {

  @Pattern(regexp = "^(web|app|WEB|APP)$")
  @NotEmpty
  protected String channelId;

  @NotEmpty
  protected String traceId;
}