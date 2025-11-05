package com.demo.service.entrypoint.auth.params.refresh;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenParam implements Serializable {

  @NotEmpty
  private String refreshToken;
}
