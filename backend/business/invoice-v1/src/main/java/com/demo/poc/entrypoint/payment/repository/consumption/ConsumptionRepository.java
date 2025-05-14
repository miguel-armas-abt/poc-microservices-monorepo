package com.demo.poc.entrypoint.payment.repository.consumption;

import com.demo.poc.entrypoint.payment.repository.consumption.entity.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
