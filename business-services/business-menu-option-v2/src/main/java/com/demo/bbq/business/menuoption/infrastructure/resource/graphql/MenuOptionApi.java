package com.demo.bbq.business.menuoption.infrastructure.resource.graphql;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import org.eclipse.microprofile.graphql.*;

@GraphQLApi
public class MenuOptionApi {

  @Inject
  MenuOptionService menuOptionService;

  @Query
  public Uni<MenuOption> findById(@Name("menuOptionId") Long id) {
    return menuOptionService.findById(id);
  }

  @Query("findByCategory")
  @Description("Get menu options by category")
  public Multi<MenuOption> findByCategory(@Name("categoryCode") String categoryCode) {
    return menuOptionService.findByCategory(categoryCode);
  }

  @Mutation
  public Uni<Long> save(MenuOptionRequest menuOptionRequest) {
    return menuOptionService.save(menuOptionRequest);
  }

  @Mutation
  public Uni<Long> update(MenuOptionRequest menuOptionRequest, Long id) {
    return menuOptionService.update(menuOptionRequest, id);
  }

  @Mutation
  public Uni<Boolean> deleteById(Long id) {
    return menuOptionService.deleteById(id);
  }
}
