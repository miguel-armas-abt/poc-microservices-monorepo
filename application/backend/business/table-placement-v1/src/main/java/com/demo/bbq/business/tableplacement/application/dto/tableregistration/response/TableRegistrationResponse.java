package com.demo.bbq.business.tableplacement.application.dto.tableregistration.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableRegistrationResponse implements Serializable{

  private String id;

  private Integer tableNumber;

  private Integer capacity;

  private Boolean isAvailable;
}