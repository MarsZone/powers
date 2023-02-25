package com.mars.powers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class PowersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowersApplication.class, args);
	}

}
