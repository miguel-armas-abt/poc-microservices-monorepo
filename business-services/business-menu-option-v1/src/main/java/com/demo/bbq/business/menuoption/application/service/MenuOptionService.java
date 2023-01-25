package com.demo.bbq.business.menuoption.application.service;

import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import java.util.List;

/**
 * <br/>Interface Service que define los métodos necesarios para tramitar la lógica de negocio del contexto Menu
 * Option.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public interface MenuOptionService {

  List<MenuOption> findByCategory(String categoryCode);

  MenuOption findById(Long id);

  Long save (MenuOptionRequest menuOption);

  Long update(Long id, MenuOptionRequest menuOption);

  Long deleteById(Long id);
}
