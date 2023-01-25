package com.demo.bbq.business.payment.infrastructure.broker.consumer.dto;

import lombok.*;
import java.io.Serializable;

/**
 * <br/>Clase DTO que define el modelo de objeto para transmitir información del contexto Menu Option.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable {

  private Long menuOptionId;
  private Integer quantity;
}
