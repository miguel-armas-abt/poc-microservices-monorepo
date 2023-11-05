package com.demo.bbq.business.invoice.infrastructure.repository.database.entity;

import com.demo.bbq.business.invoice.infrastructure.repository.database.catalog.DocumentType;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_type")
  private DocumentType documentType;

  @Column(name = "document_number")
  private String documentNumber;
}
