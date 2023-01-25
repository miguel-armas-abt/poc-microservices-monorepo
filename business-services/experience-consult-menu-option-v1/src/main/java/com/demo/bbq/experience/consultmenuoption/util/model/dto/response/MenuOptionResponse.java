package com.demo.bbq.experience.consultmenuoption.util.model.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

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
public class MenuOptionResponse implements Serializable {

  private Long id;

  private String name;

  private String category;

  private BigDecimal price;

}
