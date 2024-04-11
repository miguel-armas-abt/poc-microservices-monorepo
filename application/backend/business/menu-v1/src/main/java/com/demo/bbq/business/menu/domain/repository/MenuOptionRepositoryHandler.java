package com.demo.bbq.business.menu.domain.repository;

import com.demo.bbq.business.menu.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menu.domain.exception.MenuOptionException;
import com.demo.bbq.business.menu.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menu.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menu.domain.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.business.menu.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.domain.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.domain.repository.product.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MenuOptionRepositoryHandler {

  private final String PRODUCT_SCOPE = "MENU";
  private final ProductRepository productRepository;
  private final MenuOptionRepository menuOptionRepository;
  private final MenuOptionMapper menuOptionMapper;

  public List<MenuOptionResponseDTO> findAll() {
    Map<String, MenuOptionEntity> menuOptionMap = menuOptionRepository.findAll().stream()
        .collect(Collectors.toMap(MenuOptionEntity::getProductCode, Function.identity()));

    return productRepository.findByScope(PRODUCT_SCOPE).stream()
        .map(product -> menuOptionMapper.toResponseDTO(menuOptionMap.get(product.getCode()), product))
        .collect(Collectors.toList());
  }

  public void save(MenuOptionSaveRequestDTO menuOption) {
    productRepository.save(menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
    menuOptionRepository.save(menuOptionMapper.toEntity(menuOption));
  }

  public void update(String productCode, MenuOptionUpdateRequestDTO menuOption) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          MenuOptionEntity menuOptionEntity = menuOptionMapper.toEntity(menuOption, productCode);
          menuOptionEntity.setId(menuOptionFound.getId());
          return menuOptionRepository.save(menuOptionEntity);
        })
        .orElseThrow(MenuOptionException.ERROR0000::buildException);
    productRepository.update(productCode, menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
  }

  @Transactional
  public void deleteByProductCode(String productCode) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          menuOptionRepository.deleteByProductCode(productCode);
          return menuOptionFound;
        })
        .orElseThrow(MenuOptionException.ERROR0000::buildException);
    productRepository.delete(productCode);
  }

}