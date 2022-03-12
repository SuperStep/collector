package com.sust.collector.comitter;

import com.sust.collector.entity.Article;
import com.sust.collector.entity.Task;
import com.sust.collector.events.TaskConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootTest(classes = TaskConsumer.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaCommiterTest {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    TaskConsumer consumer;

    @Value("${spring.kafka.topic}")
    private String topic;

    @Test
    public void commitArticle() throws InterruptedException {
        Article article = new Article();
        article.setTitle("Title");
        article.setContent("some text");
        article.setPub_date_time(LocalDateTime.now());
        article.setPub_date(LocalDate.now());
        article.setSource("htp://test.test");
        kafkaTemplate.send(topic, article);


//        ConsumerRecord<Object, Object> singleRecord =
//                KafkaTestUtils.getSingleRecord(consumer, "tasks");
//
//        Assertions.assertEquals(article.getTitle(), ((Article)singleRecord.value()).getTitle());

    }
}