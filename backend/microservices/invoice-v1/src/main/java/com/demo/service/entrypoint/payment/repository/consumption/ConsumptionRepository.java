package com.demo.service.entrypoint.payment.repository.consumption;

import com.demo.service.entrypoint.payment.repository.consumption.entity.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
