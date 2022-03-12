package com.sust.collector;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollectorApplication {

	private static final Logger LOGGER = LogManager.getLogger(CollectorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CollectorApplication.class, args);
	}


}
