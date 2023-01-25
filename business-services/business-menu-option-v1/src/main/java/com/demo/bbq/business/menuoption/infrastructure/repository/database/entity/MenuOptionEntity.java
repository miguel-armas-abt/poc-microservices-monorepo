package com.demo.bbq.business.menuoption.infrastructure.repository.database.entity;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_options")
public class MenuOptionEntity {

  @Id
  @Column(name = "menu_option_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "description")
  private String description;

  @Column(name = "category")
  private String category;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "is_active")
  private boolean active;

}
