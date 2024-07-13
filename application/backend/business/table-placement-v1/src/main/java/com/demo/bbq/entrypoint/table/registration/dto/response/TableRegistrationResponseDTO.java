package com.demo.bbq.entrypoint.table.registration.dto.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableRegistrationResponseDTO implements Serializable{

  private String id;

  private Integer tableNumber;

  private Integer capacity;

  private Boolean isAvailable;
}