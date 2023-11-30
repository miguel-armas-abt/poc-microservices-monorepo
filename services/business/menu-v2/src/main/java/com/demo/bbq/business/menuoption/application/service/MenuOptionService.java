package com.demo.bbq.business.menuoption.application.service;

import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface MenuOptionService {

  Uni<List<MenuOption>> findByCategory(String categoryCode);

  Uni<MenuOption> findByProductCode(String productCode);

  Uni<Void> save(MenuOptionSaveRequest menuOptionRequest);

  Uni<Void> update(MenuOptionUpdateRequest menuOptionRequest, String productCode);

  Uni<Void> deleteByProductCode(String productCode);
}
