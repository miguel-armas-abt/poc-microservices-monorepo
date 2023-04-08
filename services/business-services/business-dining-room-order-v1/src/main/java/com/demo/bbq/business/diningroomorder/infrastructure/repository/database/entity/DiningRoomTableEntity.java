package com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tables")
public class DiningRoomTableEntity {

  @Id
  @Column(name = "table_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "is_available")
  private Boolean isAvailable;

  @Column(name = "table_number")
  private Integer tableNumber;

  @Column(name = "capacity")
  private Integer capacity;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "table_id", name = "table_id")
  private List<MenuOrderEntity> menuOrderList;
}
