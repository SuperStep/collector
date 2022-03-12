package com.sust.collector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class CollectorConfiguration implements Serializable {

    public CollectorConfiguration() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name, targetUrl;
    private int port;

    private String articleClass, articleStartRegex, articleEndRegex;
    private String metadataDateClass, metadataTitleClass;
    private String dateFormatFrom, dateFormatTo;
    private String dateTimeFormatFrom, dateTimeFormatTo;
    private Integer commitBatchSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CollectorConfiguration{" +
                "name='" + name + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", articleClass='" + articleClass + '\'' +
                '}';
    }
}
