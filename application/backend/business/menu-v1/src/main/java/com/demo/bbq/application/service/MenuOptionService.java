package com.demo.bbq.application.service;

import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface MenuOptionService {

  List<MenuOptionResponseDTO> findByCategory(HttpServletRequest servletRequest, String categoryCode);

  MenuOptionResponseDTO findByProductCode(HttpServletRequest servletRequest, String productCode);

  void save (HttpServletRequest servletRequest, MenuOptionSaveRequestDTO menuOption);

  void update(HttpServletRequest servletRequest, String productCode, MenuOptionUpdateRequestDTO menuOption);

  void deleteByProductCode(HttpServletRequest servletRequest, String productCode);
}
