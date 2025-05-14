package com.demo.poc.entrypoint.menu.service;

import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.poc.entrypoint.menu.mapper.MenuMapper;
import com.demo.poc.entrypoint.menu.repository.menu.MenuRepository;
import com.demo.poc.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.poc.entrypoint.menu.repository.product.ProductRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@RequiredArgsConstructor
public class MenuProductMatcher {

  private final String PRODUCT_SCOPE = "MENU";

  private final MenuRepository menuRepository;

  private final MenuMapper menuMapper;

  @RestClient
  ProductRepository productRepository;

  public Uni<List<MenuResponseDto>> findAll() {
    Uni<Map<String, MenuEntity>> menuOptionUni = menuRepository.findAllMenuOptions()
        .onItem()
        .transformToMulti(list -> Multi.createFrom().iterable(list))
        .collect().asList()
        .map(list -> list.stream()
            .collect(Collectors.toMap(MenuEntity::getProductCode, Function.identity())));

    return menuOptionUni
        .onItem()
        .transformToUni(menuOptionMap ->
            productRepository.findByScope(PRODUCT_SCOPE)
                .map(products -> products.stream()
                    .map(product -> menuMapper.toResponseDto(menuOptionMap.get(product.getCode()), product))
                    .collect(Collectors.toList())));
  }

  public Uni<Void> save(MenuSaveRequestDto menuOption) {
    return productRepository.save(menuMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE))
        .onItem()
        .transformToUni(ignore -> menuRepository.saveMenuOption(menuMapper.toEntity(menuOption)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  public Uni<Void> update(String productCode, MenuUpdateRequestDto menuOption) {
    return menuRepository.update(menuMapper.toEntity(menuOption, productCode), productCode)
        .onItem()
        .transformToUni(ignore -> productRepository.update(productCode, menuMapper.toRequestWrapper(menuOption, PRODUCT_SCOPE)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  public Uni<Void> deleteByProductCode(String productCode) {
    return menuRepository.findByProductCode(productCode)
        .flatMap(menuOptionFound -> menuRepository.deleteByProductCode(productCode))
        .onItem()
        .transformToUni(ignore -> productRepository.delete(productCode));
  }

}
