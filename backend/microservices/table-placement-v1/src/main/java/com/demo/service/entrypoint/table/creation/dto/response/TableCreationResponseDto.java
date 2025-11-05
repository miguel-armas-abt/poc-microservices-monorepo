package com.demo.service.entrypoint.table.creation.dto.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableCreationResponseDto implements Serializable{

  private String id;

  private Integer tableNumber;

  private Integer capacity;

  private Boolean isAvailable;
}