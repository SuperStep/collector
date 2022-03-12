package com.sust.collector.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "task_queue")
@Data
public class TaskQueueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean done;
    private String result;

    public TaskQueueItem() {
    }
    public TaskQueueItem(Task task) {
        this.task = task;
        this.done = false;
    }
    public TaskQueueItem(Task task, Boolean done) {
        this.task = task;
        this.done = done;
    }

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Task getTask() {
        return task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}