package com.sust.collector.jobs;

import com.sust.collector.CollectorApplication;
import com.sust.collector.entity.CollectorConfiguration;
import com.sust.collector.entity.Task;
import com.sust.collector.entity.TaskQueueItem;
import com.sust.collector.entity.TaskType;
import com.sust.collector.events.TaskConsumer;
import com.sust.collector.helpers.TestData;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringBootTest
@SpringJUnitConfig(CollectorApplication.class)
class TaskRunnerTest {

    @Autowired
    TaskRunner taskRunner;
    @Autowired
    TaskConsumer taskConsumer;

    @Test
    void createEndCompleteTask() throws InterruptedException{
        CollectorConfiguration collectorConfig = TestData.getDefaultConfig();

        Task task = new Task();
        task.setTaskType(TaskType.START);
        task.setConfiguration(collectorConfig);

        taskConsumer.commitTask(task);

        Thread.sleep(20000);
        List<TaskQueueItem> tasks = taskRunner.getWaitingTasks();
        Assert.assertEquals(0, tasks.size());

    }
}