package com.demo.bbq.business.menuoption.infrastructure.repository.handler;

import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionSaveRequest;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionUpdateRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import com.demo.bbq.business.menuoption.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menuoption.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menuoption.infrastructure.repository.restclient.ProductApi;
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
  ProductApi productApi;

  @Inject
  MenuOptionMapper menuOptionMapper;

  public Uni<List<MenuOption>> findAll() {
    Uni<Map<String, MenuOptionEntity>> menuOptionUni = menuOptionRepository.findAllMenuOptions()
        .collect().asList()
        .map(list -> list.stream()
            .collect(Collectors.toMap(MenuOptionEntity::getProductCode, Function.identity())));

    return menuOptionUni
        .onItem()
        .transformToUni(menuOptionMap ->
            productApi.findByScope(PRODUCT_SCOPE)
                .map(products -> products.stream()
                    .map(product -> menuOptionMapper.fromProductToResponse(menuOptionMap.get(product.getCode()), product))
                    .collect(Collectors.toList())));
  }

  public Uni<Void> save(MenuOptionSaveRequest menuOption) {
    return productApi.save(menuOptionMapper.fromSaveRequestToProduct(menuOption, PRODUCT_SCOPE))
        .onItem()
        .transformToUni(ignore -> menuOptionRepository.saveMenuOption(menuOptionMapper.fromSaveRequestToEntity(menuOption)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  public Uni<Void> update(String productCode, MenuOptionUpdateRequest menuOption) {
    return menuOptionRepository.update(menuOptionMapper.fromUpdateRequestToEntity(menuOption, productCode), productCode)
        .onItem()
        .transformToUni(ignore -> productApi.update(productCode, menuOptionMapper.fromUpdateRequestToProduct(menuOption, PRODUCT_SCOPE)))
        .onItem()
        .ignore()
        .andContinueWithNull();
  }

  @ReactiveTransactional
  public Uni<Void> deleteByProductCode(String productCode) {
    return menuOptionRepository.findByProductCode(productCode)
        .flatMap(menuOptionFound -> menuOptionRepository.deleteByProductCode(productCode))
        .onItem()
        .transformToUni(ignore -> productApi.delete(productCode));
  }

}
