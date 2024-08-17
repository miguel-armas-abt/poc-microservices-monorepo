package com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessorResponseWrapper implements Serializable {

  private Boolean isSuccessfulTransaction;
}
