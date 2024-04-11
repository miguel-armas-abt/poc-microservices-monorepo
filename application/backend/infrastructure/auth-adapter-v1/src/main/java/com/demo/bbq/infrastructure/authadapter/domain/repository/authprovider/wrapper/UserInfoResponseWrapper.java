package com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponseWrapper implements Serializable {

  private String sub;

  @JsonProperty("email_verified")
  private String emailVerified;

  @JsonProperty("preferred_username")
  private String preferredUsername;

  private List<String> roles;
}
