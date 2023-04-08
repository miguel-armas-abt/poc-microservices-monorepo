package com.demo.bbq.business.menuoption.application.service.impl;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menuoption.domain.catalog.MenuOptionCategory;
import com.demo.bbq.business.menuoption.domain.exception.MenuOptionException;
import com.demo.bbq.business.menuoption.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.support.logstash.Markers;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuOptionServiceImpl implements MenuOptionService {

  private final MenuOptionRepository menuOptionRepository;
  private final MenuOptionMapper menuOptionMapper;

  @Override
  public List<MenuOption> findByCategory(String categoryCode) {
    return (Optional.ofNullable(categoryCode).isEmpty()
          ? menuOptionRepository.findAll()
          : this.validateMenuOptionAndFindByCategory(categoryCode))
        .stream()
        .map(menuOptionMapper::fromEntityToResponse)
        .peek(menuOption -> log.info(Markers.SENSITIVE_JSON, "findByCategory: {}", new Gson().toJson(menuOption)))
        .collect(Collectors.toList());
  }

  private List<MenuOptionEntity> validateMenuOptionAndFindByCategory(String categoryCode) {
    MenuOptionCategory.validateCategory.accept(categoryCode);
    return menuOptionRepository.findByCategory(categoryCode);
  }

  @Override
  public MenuOption findById(Long id) {
    return menuOptionRepository.findById(id)
        .map(menuOptionMapper::fromEntityToResponse)
        .orElseThrow(MenuOptionException.ERROR1000::buildCustomException);
  }

  @Override
  public Long save(MenuOptionRequest menuOption) {
    return menuOptionRepository.save(menuOptionMapper.fromRequestToEntity(menuOption)).getId();
  }

  @Override
  public Long update(Long id, MenuOptionRequest menuOption) {
    return menuOptionRepository.findById(id)
      .map(menuOptionFound -> {
        MenuOptionEntity menuOptionEntity = menuOptionMapper.fromRequestToEntity(menuOption);
        menuOptionEntity.setId(id);
        menuOptionRepository.save(menuOptionEntity);
        return menuOptionEntity.getId();
      })
      .orElseThrow(MenuOptionException.ERROR1000::buildCustomException);
  }

  @Override
  public Long deleteById(Long id) {
    return menuOptionRepository.findById(id)
        .map(menuOptionFound -> {
          menuOptionRepository.deleteById(id);
          return menuOptionFound.getId();
        })
        .orElseThrow(MenuOptionException.ERROR1000::buildCustomException);
  }

}
