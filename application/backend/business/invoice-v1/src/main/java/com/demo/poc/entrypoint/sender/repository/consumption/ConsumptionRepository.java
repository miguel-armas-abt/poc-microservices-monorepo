package com.demo.poc.entrypoint.sender.repository.consumption;

import com.demo.poc.entrypoint.sender.repository.consumption.entity.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
