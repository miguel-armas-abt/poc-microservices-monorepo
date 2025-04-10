package com.demo.poc.entrypoint.menu.service;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;

import java.util.List;
import java.util.Map;

public interface MenuService {

  List<MenuResponseDto> findByCategory(Map<String, String> headers, String categoryCode);

  MenuResponseDto findByProductCode(Map<String, String> headers, String productCode);

  void save(Map<String, String> headers, MenuSaveRequestDto menuOption);

  void update(Map<String, String> headers, String productCode, MenuUpdateRequestDto menuOption);

  void deleteByProductCode(Map<String, String> headers, String productCode);
}
