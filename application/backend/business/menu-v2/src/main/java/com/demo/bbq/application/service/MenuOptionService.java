package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.Map;

public interface MenuOptionService {

  Multi<MenuOptionResponseDTO> findByCategory(String categoryCode, Map<String, String> headers);

  Uni<MenuOptionResponseDTO> findByProductCode(String productCode, Map<String, String> headers);

  Uni<Void> save(MenuOptionSaveRequestDTO menuOptionRequest, Map<String, String> headers);

  Uni<Void> update(MenuOptionUpdateRequestDTO menuOptionRequest, String productCode, Map<String, String> headers);

  Uni<Void> deleteByProductCode(String productCode, Map<String, String> headers);
}
