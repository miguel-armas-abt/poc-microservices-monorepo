package com.demo.bbq.entrypoint.menu.service;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.response.MenuResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface MenuService {

  List<MenuResponseDTO> findByCategory(HttpServletRequest servletRequest, String categoryCode);

  MenuResponseDTO findByProductCode(HttpServletRequest servletRequest, String productCode);

  void save (HttpServletRequest servletRequest, MenuSaveRequestDTO menuOption);

  void update(HttpServletRequest servletRequest, String productCode, MenuUpdateRequestDTO menuOption);

  void deleteByProductCode(HttpServletRequest servletRequest, String productCode);
}
