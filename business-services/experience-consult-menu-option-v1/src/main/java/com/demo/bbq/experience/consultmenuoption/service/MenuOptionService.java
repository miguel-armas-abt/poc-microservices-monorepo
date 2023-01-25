package com.demo.bbq.experience.consultmenuoption.service;

import com.demo.bbq.experience.consultmenuoption.util.model.dto.request.MenuOptionRequest;
import com.demo.bbq.experience.consultmenuoption.util.model.dto.response.MenuOptionResponse;
import java.util.List;

/**
 * <br/>Interface Service que define los métodos necesarios para tramitar la lógica de negocio del contexto Menu
 * Option.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public interface MenuOptionService {

  List<MenuOptionResponse> findByCategory(String name);

  MenuOptionResponse findById(Long id);

  void save (MenuOptionRequest menuOption);

  void update(Long id, MenuOptionRequest menuOption);

  void deleteById(Long id);
}
