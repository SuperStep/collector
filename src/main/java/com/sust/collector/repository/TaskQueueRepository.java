package com.sust.collector.repository;

import com.sust.collector.entity.TaskQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskQueueRepository extends JpaRepository<TaskQueueItem, Long> {

    List<TaskQueueItem> getAllByDoneIsFalse();

}