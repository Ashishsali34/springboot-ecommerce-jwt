package com.ecommerce.sb_ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.ecommerce.sb_ecom")
//@ComponentScan(basePackages = "com.ecommerce.sb_ecom")
public class SbEcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbEcomApplication.class, args);
	}

}
