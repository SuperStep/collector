package com.sust.collector.events;

import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public abstract class AbstractConsumer {

    protected CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }
}
