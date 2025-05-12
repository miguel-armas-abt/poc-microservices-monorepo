package com.demo.poc.entrypoint.menu.service;

import static com.demo.poc.entrypoint.menu.constant.ParameterConstant.*;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public interface MenuService {

  Multi<MenuResponseDto> findByCategory(@Pattern(regexp = CATEGORY_REGEX) String categoryCode);

  Uni<MenuResponseDto> findByProductCode(@NotEmpty String productCode);

  Uni<Void> save(@Valid MenuSaveRequestDto menuOptionRequest);

  Uni<Void> update(@Valid MenuUpdateRequestDto menuOptionRequest,
                   @NotEmpty String productCode);

  Uni<Void> deleteByProductCode(@NotEmpty String productCode);
}