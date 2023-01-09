package com.urnaelectoral.urnaelectoral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UrnaelectoralApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrnaelectoralApplication.class, args);
	}

}
