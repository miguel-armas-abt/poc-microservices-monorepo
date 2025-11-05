package com.demo.service.entrypoint.search.params;

import java.io.Serializable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentNumberParam implements Serializable {

  @NotEmpty
  private String documentNumber;

  @NotEmpty
  private String documentType;
}