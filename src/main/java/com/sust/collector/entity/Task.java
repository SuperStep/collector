package com.sust.collector.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private CollectorConfiguration configuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType=" + taskType +
                ", configuration=" + configuration +
                '}';
    }
}


