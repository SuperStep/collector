package com.sust.collector.config;

import com.sust.collector.jobs.TaskRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("com.sust.collector.jobs")
public class ScheduledConfig {
}
