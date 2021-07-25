package com.example.activiti.events;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiEventsApp {

	public static void main(String[] args){
		SpringApplication.run(ActivitiEventsApp.class, args);
	}

}
