package ru.skillbox.snbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SnbotApplication {
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "snbot");
		SpringApplication.run(SnbotApplication.class, args);
	}
}
