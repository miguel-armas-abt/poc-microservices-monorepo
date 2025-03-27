package com.demo.poc.entrypoint.table.placement.repository.document;

import java.io.Serializable;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tables")
public class TableDocument implements Serializable {

  @Id
  private String id;

  private Integer tableNumber;

  private Integer capacity;

  private Boolean isAvailable;

  private List<MenuOrderDocument> menuOrderList;
}
