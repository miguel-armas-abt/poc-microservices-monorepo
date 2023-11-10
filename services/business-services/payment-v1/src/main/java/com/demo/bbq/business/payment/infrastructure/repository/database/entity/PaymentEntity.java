package com.demo.bbq.business.payment.infrastructure.repository.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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

  @Column(name = "paymentStatus")
  private String paymentStatus;

  @Column(name = "invoiceId")
  private Long invoiceId;
}
