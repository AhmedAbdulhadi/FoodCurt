package com.novent.foodordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer{

//	// for JAR
//	public static void main(String[] args) {
//		SpringApplication.run(DemoApplicationTests.class, args);
//	}
	
	// for JAR
		public static void main(String[] args) {
			SpringApplication.run(DemoApplication.class, args);
		}

	// for WAR
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		 return builder.sources(DemoApplication.class);
	}
}
