package com.demo.bbq.repository.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "totalAmount")
  private BigDecimal totalAmount;

  @Column(name = "paymentMethod")
  private String paymentMethod;

  @Column(name = "invoiceId")
  private Long invoiceId;
}
