package com.Soganis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.Soganis")
public class SoganisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoganisApplication.class, args);
	}

}
