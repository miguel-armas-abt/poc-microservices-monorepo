package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;

import java.util.List;
import java.util.Map;

public interface MenuService {

  List<MenuResponseDTO> findByCategory(Map<String, String> headers, String categoryCode);

  MenuResponseDTO findByProductCode(Map<String, String> headers, String productCode);

  void save(Map<String, String> headers, MenuSaveRequestDTO menuOption);

  void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption);

  void deleteByProductCode(Map<String, String> headers, String productCode);
}
