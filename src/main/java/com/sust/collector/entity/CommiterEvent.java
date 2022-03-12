package com.sust.collector.entity;

import lombok.Data;

@Data
public class CommiterEvent {

    private EventType eventType;
    private String reference;
    private Object object;

    public CommiterEvent(EventType eventType, String reference, Object object) {
        this.eventType = eventType;
        this.reference = reference;
        this.object = object;
    }

}


