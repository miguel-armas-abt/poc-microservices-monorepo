package com.demo.bbq.repository.consumption;

import com.demo.bbq.repository.consumption.entity.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
