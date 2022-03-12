package com.sust.collector.comitter;

import com.norconex.committer.core.AbstractMappedCommitter;
import com.norconex.committer.core.IAddOperation;
import com.norconex.committer.core.ICommitOperation;
import com.norconex.committer.core.IDeleteOperation;
import com.norconex.commons.lang.map.Properties;
import com.sust.collector.entity.Article;
import com.sust.collector.entity.CommiterEvent;
import com.sust.collector.entity.EventType;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class KafkaCommiter extends AbstractMappedCommitter {

    private static final Logger LOGGER = LogManager.getLogger(KafkaCommiter.class);
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private static String TOPIC = "articles";

    public KafkaCommiter(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    protected void saveToXML(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {

    }

    @Override
    protected void loadFromXml(XMLConfiguration xmlConfiguration) {

    }

    @Override
    protected void commitBatch(List<ICommitOperation> batch) {
        try {
            for (ICommitOperation op: batch) {
                if (op instanceof IAddOperation){
                    commitAddOperation((IAddOperation) op);
                } else if (op instanceof IDeleteOperation){
                    commitDeleteOperation((IDeleteOperation) op);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error committing to mongodb: ", e);
        }
    }

    private void commitDeleteOperation(IDeleteOperation delete) {

        kafkaTemplate.send(TOPIC, new CommiterEvent(
                EventType.DELETE,
                delete.getReference(),
                null));
    }

    private void commitAddOperation(IAddOperation add) throws IOException {

        Properties metadata = add.getMetadata();

        String docId = add.getMetadata().getString(getTargetReferenceField());
        if (StringUtils.isBlank(docId)) {
            docId = add.getReference();
        }

        Article article = new Article();

        String content = IOUtils.toString(add.getContentStream(), StandardCharsets.UTF_8.name());
        article.setContent(content);

        if (metadata.get("source") != null){
            article.setSource(metadata.get("source").get(0));
        }
        if (metadata.get("title") != null){
            article.setTitle(metadata.get("title").get(0));
        }
        if (metadata.get("pub_date") != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(metadata.get("pub_date").get(0), formatter);
            article.setPub_date(date);
        }
        if (metadata.get("pub_date_time") != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(metadata.get("pub_date_time").get(0), formatter);
            article.setPub_date_time(dateTime);
        }

        kafkaTemplate.send(TOPIC, new CommiterEvent(
                EventType.ADD,
                add.getReference(),
                article));
    }
}
