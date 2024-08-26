package com.demo.bbq.entrypoint.inquiry.dto.params;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
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