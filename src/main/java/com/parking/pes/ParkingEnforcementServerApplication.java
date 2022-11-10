package com.parking.pes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingEnforcementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingEnforcementServerApplication.class, args);
	}

}
