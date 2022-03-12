package com.sust.collector.events;

import com.sust.collector.entity.CollectorConfiguration;
import com.sust.collector.entity.Task;
import com.sust.collector.entity.TaskType;
import com.sust.collector.helpers.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest()
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class TaskConsumerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    TaskConsumer taskConsumer;

    @Test
    public void startTask() throws InterruptedException {

        CollectorConfiguration collectorConfig = TestData.getDefaultConfig();

        Task task = new Task();
        task.setTaskType(TaskType.START);
        task.setConfiguration(collectorConfig);

        kafkaTemplate.send("tasks", task);

        taskConsumer.getLatch().await(10, TimeUnit.SECONDS);
    }
}