package com.sust.collector.entity;

import lombok.Data;
import org.apache.tika.config.Field;
import org.springframework.stereotype.Indexed;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Article implements Serializable {

    private String source, content;
    private LocalDate pub_date;
    private LocalDateTime pub_date_time;
    private String title;
    public Article(){}

}