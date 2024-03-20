package com.demo.bbq.business.menu.application.service;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.application.dto.response.MenuOption;
import java.util.List;

public interface MenuOptionService {

  List<MenuOption> findByCategory(String categoryCode);

  MenuOption findByProductCode(String productCode);

  void save (MenuOptionSaveRequest menuOption);

  void update(String productCode, MenuOptionUpdateRequest menuOption);

  void deleteByProductCode(String productCode);
}
