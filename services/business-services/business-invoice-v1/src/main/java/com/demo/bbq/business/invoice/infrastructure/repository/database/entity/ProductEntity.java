package com.demo.bbq.business.invoice.infrastructure.repository.database.entity;

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
@Table(name = "products")
public class ProductEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "total")
  private BigDecimal total;

  @Column(name = "invoice_id")
  private Long invoiceId;
}
