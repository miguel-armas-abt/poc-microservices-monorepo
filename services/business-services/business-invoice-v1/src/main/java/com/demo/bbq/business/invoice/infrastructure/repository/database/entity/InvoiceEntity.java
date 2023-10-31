package com.demo.bbq.business.invoice.infrastructure.repository.database.entity;

import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentMethod;
import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.PaymentStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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

  @Column(name = "due_date")
  private String dueDate;

  @OneToOne
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "invoice_id", name = "invoice_id")
  private List<ProductEntity> productEntityList;

  @Column(name = "subtotal")
  private BigDecimal subtotal;

  @Column(name = "igv")
  private BigDecimal igv;

  @Column(name = "total")
  private BigDecimal total;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status")
  private PaymentStatus paymentStatus;

  @Column(name = "payment_installments")
  private Integer paymentInstallments;
}
