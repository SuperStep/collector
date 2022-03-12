package com.sust.collector.events;

import com.sust.collector.entity.CollectorConfiguration;
import com.sust.collector.entity.Task;
import com.sust.collector.entity.TaskQueueItem;
import com.sust.collector.repository.CollectorConfigurationRepository;
import com.sust.collector.repository.TaskQueueRepository;
import com.sust.collector.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class TaskConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskConsumer.class);

    public CountDownLatch getLatch() {
        return latch;
    }

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskQueueRepository taskQueueRepository;
    @Autowired
    CollectorConfigurationRepository configurationRepository;

    @KafkaListener(topics = "tasks")
    public void receive(Task task) {
        LOGGER.debug("Consuming task: {} - {}", task.getTaskType(), task.getConfiguration());
        try{
            commitTask(task);
            latch.countDown();
            LOGGER.debug("Task {} - {} saved to task queue", task.getTaskType(), task.getConfiguration());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }

    public TaskQueueItem commitTask(Task task) {
        CollectorConfiguration collectorConfiguration = task.getConfiguration();
        configurationRepository.save(collectorConfiguration);
        taskRepository.save(task);
        return taskQueueRepository.save(new TaskQueueItem(task));
    }

}
