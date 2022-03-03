package com.fetcher.booksmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BooksMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksMicroserviceApplication.class, args);
	}

}
