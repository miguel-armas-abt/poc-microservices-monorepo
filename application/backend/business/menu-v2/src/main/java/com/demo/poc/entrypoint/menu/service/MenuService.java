package com.demo.poc.entrypoint.menu.service;

import static com.demo.poc.entrypoint.menu.constant.ParameterConstant.*;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public interface MenuService {

  Multi<MenuResponseDTO> findByCategory(@Pattern(regexp = CATEGORY_REGEX) String categoryCode);

  Uni<MenuResponseDTO> findByProductCode(@NotEmpty String productCode);

  Uni<Void> save(@Valid MenuSaveRequestDTO menuOptionRequest);

  Uni<Void> update(@Valid MenuUpdateRequestDTO menuOptionRequest,
                   @NotEmpty String productCode);

  Uni<Void> deleteByProductCode(@NotEmpty String productCode);
}