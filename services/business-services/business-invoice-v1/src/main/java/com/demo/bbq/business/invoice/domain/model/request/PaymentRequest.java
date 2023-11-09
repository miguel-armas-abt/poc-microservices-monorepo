package com.demo.bbq.business.invoice.domain.model.request;

import com.demo.bbq.business.invoice.domain.constant.InvoiceRegex;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {

  private List<ProductRequest> productList;

  private Customer customer;

  @Pattern(regexp = InvoiceRegex.PAYMENT_METHOD, message = "Invalid payment method")
  private String paymentMethod;
}
