package com.demo.bbq.entrypoint.menu.service;

import static com.demo.bbq.entrypoint.menu.constant.ConstraintConstant.*;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public interface MenuService {

  Multi<MenuResponseDTO> findByCategory(
      @Pattern(regexp = CATEGORY_REGEX, message = CATEGORY_INVALID_MESSAGE)
      String categoryCode);

  Uni<MenuResponseDTO> findByProductCode(
      @NotBlank(message = PRODUCT_CODE_NOT_BLANK_MESSAGE)
      String productCode);

  Uni<Void> save(
      @Valid MenuSaveRequestDTO menuOptionRequest);

  Uni<Void> update(
      @Valid MenuUpdateRequestDTO menuOptionRequest,

      @NotBlank(message = PRODUCT_CODE_NOT_BLANK_MESSAGE)
      String productCode);

  Uni<Void> deleteByProductCode(
      @NotBlank(message = PRODUCT_CODE_NOT_BLANK_MESSAGE)
      String productCode);
}