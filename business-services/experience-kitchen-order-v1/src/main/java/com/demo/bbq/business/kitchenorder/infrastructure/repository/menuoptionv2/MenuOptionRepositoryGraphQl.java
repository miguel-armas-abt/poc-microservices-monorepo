package com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2;

import com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2.dto.MenuOptionDto;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.core.Field;
import io.smallrye.graphql.client.core.Operation;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MenuOptionRepositoryGraphQl {

  @Inject
  @GraphQLClient("menu-option")
  DynamicGraphQLClient graphQlClient;

  public Uni<MenuOptionDto> findById(Long id) {
    Document query = Document.document(
        Operation.operation(
            Field.field("findById(menuOptionId: " + id +")",
                Field.field("id"),
                Field.field("description"),
                Field.field("category"),
                Field.field("price"),
                Field.field("active"))));

    return graphQlClient.executeAsync(query)
        .map(response -> response.getObject(MenuOptionDto.class, "findById"));
  }

  // error
  public Multi<MenuOptionDto> findByCategory(String categoryCode) {
    Document query = Document.document(
        Operation.operation(
            Field.field("findByCategory(categoryCode: " + categoryCode +")",
                Field.field("id"),
                Field.field("description"),
                Field.field("category"),
                Field.field("price"),
                Field.field("active"))));

    return graphQlClient.subscription(query)
        .map(response -> response.getObject(MenuOptionDto.class, "findByCategory"));
  }

}
