package com.demo.bbq.business.menuoption.application.service;

import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.response.MenuOptionResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface MenuOptionService {

  Multi<MenuOptionResponseDTO> findByCategory(String categoryCode);

  Uni<MenuOptionResponseDTO> findByProductCode(String productCode);

  Uni<Void> save(MenuOptionSaveRequestDTO menuOptionRequest);

  Uni<Void> update(MenuOptionUpdateRequestDTO menuOptionRequest, String productCode);

  Uni<Void> deleteByProductCode(String productCode);
}
