package com.demo.bbq.repository;

import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.application.mapper.MenuOptionMapper;
import com.demo.bbq.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.repository.product.ProductRepository;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
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

  public List<MenuOptionResponseDTO> findAll(HttpServletRequest servletRequest) {
    Map<String, MenuOptionEntity> menuOptionMap = menuOptionRepository.findAll().stream()
        .collect(Collectors.toMap(MenuOptionEntity::getProductCode, Function.identity()));

    return productRepository.findByScope(servletRequest, PRODUCT_SCOPE)
        .stream()
        .map(product -> menuOptionMapper.toResponseDTO(menuOptionMap.get(product.getCode()), product))
        .collect(Collectors.toList());
  }

  public void save(HttpServletRequest servletRequest, MenuOptionSaveRequestDTO menuOption) {
    productRepository.save(servletRequest, menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
    menuOptionRepository.save(menuOptionMapper.toEntity(menuOption));
  }

  public void update(HttpServletRequest servletRequest, String productCode, MenuOptionUpdateRequestDTO menuOption) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          MenuOptionEntity menuOptionEntity = menuOptionMapper.toEntity(menuOption, productCode);
          menuOptionEntity.setId(menuOptionFound.getId());
          return menuOptionRepository.save(menuOptionEntity);
        })
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
    productRepository.update(servletRequest, productCode, menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
  }

  @Transactional
  public void deleteByProductCode(HttpServletRequest servletRequest, String productCode) {
    menuOptionRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          menuOptionRepository.deleteByProductCode(productCode);
          return menuOptionFound;
        })
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
    productRepository.delete(servletRequest, productCode);
  }

}