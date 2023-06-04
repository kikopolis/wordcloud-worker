package com.kikopolis.wordcloudworker.repository;

import com.kikopolis.wordcloudworker.entity.WorkOrder;
import org.springframework.data.repository.CrudRepository;

public interface WorkOrderRepository extends CrudRepository<WorkOrder, Long> {
}
