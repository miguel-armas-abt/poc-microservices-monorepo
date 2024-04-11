package com.demo.bbq.business.menuoption.domain.repository.menuoption.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import javax.persistence.*;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_options")
public class MenuOptionEntity extends PanacheEntityBase {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_code", unique = true)
  private String productCode;

  @Column(name = "description")
  private String description;

  @Column(name = "category")
  private String category;
}
