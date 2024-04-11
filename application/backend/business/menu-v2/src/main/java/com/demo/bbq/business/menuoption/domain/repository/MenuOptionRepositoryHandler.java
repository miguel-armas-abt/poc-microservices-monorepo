package com.demo.bbq.business.menuoption.domain.repository;

import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.business.menuoption.application.mapper.MenuOptionMapper;
import com.demo.bbq.business.menuoption.domain.repository.menuoption.MenuOptionRepository;
import com.demo.bbq.business.menuoption.domain.repository.menuoption.entity.MenuOptionEntity;
import com.demo.bbq.business.menuoption.domain.repository.product.ProductRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MenuOptionRepositoryHandler {

  private final String PRODUCT_SCOPE = "MENU";

  @Inject
  MenuOptionRepository menuOptionRepository;

  @RestClient
  ProductRepository productRepository;

  @Inject
  MenuOptionMapper menuOptionMapper;

  public Uni<List<MenuOptionResponseDTO>> findAll() {
    Uni<Map<String, MenuOptionEntity>> menuOptionUni = menuOptionRepository.findAllMenuOptions()
        .collect().asList()
        .map(list -> list.stream()
            .collect(Collectors.toMap(MenuOptionEntity::getProductCode, Function.identity())));

    return menuOptionUni
        .onItem()
        .transformToUni(menuOptionMap ->
            productRepository.findByScope(PRODUCT_SCOPE)
                .map(products -> products.stream()
                    .map(product -> menuOptionMapper.toResponseDTO(menuOptionMap.get(product.getCode()), product))
                    .collect(Collectors.toList())));
  }

  public Uni<Void> save(MenuOptionSaveRequestDTO menuOption) {
    return productRepository.save(menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE))
        .onItem()
        .transformToUni(ignore -> menuOptionRepository.saveMenuOption(menuOptionMapper.toEntity(menuOption)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  public Uni<Void> update(String productCode, MenuOptionUpdateRequestDTO menuOption) {
    return menuOptionRepository.update(menuOptionMapper.toEntity(menuOption, productCode), productCode)
        .onItem()
        .transformToUni(ignore -> productRepository.update(productCode, menuOptionMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  @ReactiveTransactional
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuOptionRepository.findByProductCode(productCode)
        .flatMap(menuOptionFound -> menuOptionRepository.deleteByProductCode(productCode))
        .onItem()
        .transformToUni(ignore -> productRepository.delete(productCode));
  }

}
