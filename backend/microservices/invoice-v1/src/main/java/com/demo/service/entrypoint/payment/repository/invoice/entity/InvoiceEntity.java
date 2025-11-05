package com.demo.service.entrypoint.payment.repository.invoice.entity;

import com.demo.service.entrypoint.payment.repository.customer.entity.CustomerEntity;
import com.demo.service.entrypoint.payment.repository.consumption.entity.ConsumptionEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class InvoiceEntity implements Serializable {

  @Id
  @Column(name = "invoice_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "billing_date")
  private String billingDate;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(referencedColumnName = "invoice_id", name = "invoice_id")
  private List<ConsumptionEntity> productList;

  @Column(name = "igv")
  private BigDecimal igv;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status")
  private PaymentStatus paymentStatus;
}
