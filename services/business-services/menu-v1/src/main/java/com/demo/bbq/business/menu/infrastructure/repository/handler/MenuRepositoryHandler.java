package com.demo.bbq.business.menu.infrastructure.repository.handler;

import com.demo.bbq.business.menu.domain.model.response.MenuOption;
import com.demo.bbq.business.menu.infrastructure.mapper.MenuOptionMapper;
import com.demo.bbq.business.menu.infrastructure.repository.cache.service.MenuOptionCache;
import com.demo.bbq.business.menu.infrastructure.repository.database.MenuOptionRepository;
import com.demo.bbq.business.menu.infrastructure.repository.database.entity.MenuOptionEntity;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.ProductApi;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryHandler {

  private final ProductApi productApi;
  private final MenuOptionRepository menuOptionRepository;

  private final MenuOptionCache menuOptionCache;
  private final MenuOptionMapper menuOptionMapper;

  private List<MenuOption> findAll() {
    List<ProductDto> productList = productApi.findByScope("MENU").blockingGet();
    List<MenuOptionEntity> menuOptionList = menuOptionRepository.findAll();

//    productList.stream().peek(product -> {
//          menuOptionList.stream().filter(menuOption -> menuOption.getProductCode().equals(product.getCode()))
//              .findFirst()
//              .map(menuOption -> menuOptionMapper.fromProductToResponse(menuOption, product))
//              .ifPresent(menuOptionCache::put);
//        });

    return null;
  }
}
