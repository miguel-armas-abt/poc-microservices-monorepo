package com.demo.bbq.business.invoice.domain.repository.consumption;

import com.demo.bbq.business.invoice.domain.repository.consumption.entity.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
