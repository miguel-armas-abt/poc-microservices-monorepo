package com.demo.bbq.repository.menuoption.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import javax.persistence.*;
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
