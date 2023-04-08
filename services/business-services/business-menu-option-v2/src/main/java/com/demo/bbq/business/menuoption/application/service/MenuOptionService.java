package com.demo.bbq.business.menuoption.application.service;

import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface MenuOptionService {

  Uni<MenuOption> findById(Long id);

  Multi<MenuOption> findByCategory(String categoryCode);

  Uni<Long> save(MenuOptionRequest menuOptionRequest);

  Uni<Long> update(MenuOptionRequest menuOptionRequest, Long id);

  Uni<Boolean> deleteById(Long id);
}
