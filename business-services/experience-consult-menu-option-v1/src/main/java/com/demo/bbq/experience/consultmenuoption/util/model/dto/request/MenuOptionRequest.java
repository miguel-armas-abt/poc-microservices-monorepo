package com.demo.bbq.experience.consultmenuoption.util.model.dto.request;

import com.demo.bbq.experience.consultmenuoption.util.constant.RegexConstant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
public class MenuOptionRequest implements Serializable {

  @Pattern(regexp = RegexConstant.ANY_STRING)
  @Size(min = 3, max = 300)
  @NotNull(message = "name cannot be null")
  private String name;

  @Pattern(regexp = RegexConstant.ANY_STRING)
  @Size(min = 3, max = 300)
  @NotNull(message = "name cannot be null")
  private String category;

  @NotNull(message = "price cannot be null")
  private BigDecimal price;

  @NotNull(message = "is active cannot be null")
  private boolean isActive;

}