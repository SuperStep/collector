package com.sust.collector.jobs;

import com.sust.collector.entity.CollectorConfiguration;
import com.sust.collector.entity.Task;
import com.sust.collector.entity.TaskQueueItem;
import com.sust.collector.events.TaskConsumer;
import com.sust.collector.repository.TaskQueueRepository;
import com.sust.collector.repository.TaskRepository;
import com.sust.collector.service.CollectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Async
@Component
public class TaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    public List<TaskQueueItem> getWaitingTasks() {
        return taskQueueRepository.getAllByDoneIsFalse();
    }

    @Autowired
    TaskQueueRepository taskQueueRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    CollectorService collectorService;
    @Autowired
    TaskConsumer taskConsumer;

    @Async
    @Scheduled(fixedRate = 1000)
    public void runAllTasks(){
        List<TaskQueueItem> tasks = taskQueueRepository.getAllByDoneIsFalse();
        for (TaskQueueItem taskItem : tasks) {
            executeTask(taskItem);
        }
    }

    private void executeTask(TaskQueueItem taskItem) {
        Task task = taskItem.getTask();

        CollectorConfiguration configuration = task.getConfiguration();
        try {
            collectorService.configure(configuration);
            LOGGER.info("Executing: {}", task);
            collectorService.start();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            taskItem.setResult(e.getMessage());
        } finally {
            taskItem.setDone(true);
            taskQueueRepository.save(taskItem);
        }
    }

}
