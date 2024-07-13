package com.demo.bbq.entrypoint.sender.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO implements Serializable {
  private String method;
}
