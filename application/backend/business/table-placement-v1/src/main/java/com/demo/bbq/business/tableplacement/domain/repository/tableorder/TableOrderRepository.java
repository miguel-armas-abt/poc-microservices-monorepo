package com.demo.bbq.business.tableplacement.domain.repository.tableorder;

import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TableOrderRepository extends ReactiveMongoRepository<TableDocument, String> {

  Mono<TableDocument> findByTableNumber(Integer tableNumber);

}