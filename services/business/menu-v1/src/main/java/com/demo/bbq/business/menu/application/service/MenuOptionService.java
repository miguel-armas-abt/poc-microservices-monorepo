package com.demo.bbq.business.menu.application.service;

import com.demo.bbq.business.menu.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menu.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import java.util.List;

public interface MenuOptionService {

  List<MenuOption> findByCategory(String categoryCode);

  MenuOption findByProductCode(String productCode);

  void save (MenuOptionSaveRequest menuOption);

  void update(String productCode, MenuOptionUpdateRequest menuOption);

  void deleteByProductCode(String productCode);
}
