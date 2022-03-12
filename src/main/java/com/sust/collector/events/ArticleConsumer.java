package com.sust.collector.events;

import com.sust.collector.entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Deprecated
public class ArticleConsumer extends AbstractConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleConsumer.class);

    @Value("${spring.kafka.topic}")
    private String topic;

    private Article article = null;

    @KafkaListener(topics = "article")
    public void receive(Article article) {
        LOGGER.debug(article.getTitle());
        this.article = article;
        this.latch.countDown();
    }

    public Article getArticle() {
        return article;
    }
}