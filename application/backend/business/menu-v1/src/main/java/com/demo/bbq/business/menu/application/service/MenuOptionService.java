package com.demo.bbq.business.menu.application.service;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponseDTO;

import java.util.List;

public interface MenuOptionService {

  List<MenuOptionResponseDTO> findByCategory(String categoryCode);

  MenuOptionResponseDTO findByProductCode(String productCode);

  void save (MenuOptionSaveRequestDTO menuOption);

  void update(String productCode, MenuOptionUpdateRequestDTO menuOption);

  void deleteByProductCode(String productCode);
}
