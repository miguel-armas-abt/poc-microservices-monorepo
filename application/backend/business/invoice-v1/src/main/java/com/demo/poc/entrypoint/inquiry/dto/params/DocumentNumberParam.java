package com.demo.poc.entrypoint.inquiry.dto.params;

import com.demo.poc.commons.core.validations.params.DefaultParams;
import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentNumberParam extends DefaultParams implements Serializable {

  @NotEmpty
  private String documentNumber;

  @NotEmpty
  private String documentType;
}