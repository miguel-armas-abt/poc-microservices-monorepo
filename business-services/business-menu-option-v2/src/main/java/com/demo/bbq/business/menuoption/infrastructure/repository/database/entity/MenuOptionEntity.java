package com.demo.bbq.business.menuoption.infrastructure.repository.database.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu_options")
public class MenuOptionEntity extends PanacheEntity {

  @Column(name = "description")
  private String description;

  @Column(name = "category")
  private String category;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "is_active")
  private boolean active;
}
