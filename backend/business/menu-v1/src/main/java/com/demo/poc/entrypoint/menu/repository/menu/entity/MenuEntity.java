package com.demo.poc.entrypoint.menu.repository.menu.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_options")
public class MenuEntity extends PanacheEntityBase {

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
