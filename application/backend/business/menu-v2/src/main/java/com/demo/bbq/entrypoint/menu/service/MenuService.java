package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface MenuService {

  Multi<MenuResponseDTO> findByCategory(String categoryCode);

  Uni<MenuResponseDTO> findByProductCode(String productCode);

  Uni<Void> save(MenuSaveRequestDTO menuOptionRequest);

  Uni<Void> update(MenuUpdateRequestDTO menuOptionRequest, String productCode);

  Uni<Void> deleteByProductCode(String productCode);
}
