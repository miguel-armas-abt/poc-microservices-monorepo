package com.demo.poc.entrypoint.table.placement.repository;

import com.demo.poc.entrypoint.table.placement.repository.document.TableDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TableOrderRepository extends ReactiveMongoRepository<TableDocument, String> {

  Mono<TableDocument> findByTableNumber(Integer tableNumber);

}