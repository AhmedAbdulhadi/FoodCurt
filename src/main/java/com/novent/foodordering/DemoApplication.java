package com.novent.foodordering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);
	// for JAR
		public static void main(String[] args) {
			System.out.println("Demo app appli.prop");
			LOGGER.info("Demo app appli.prop");
			System.out.println(System.getenv("database"));
			LOGGER.info(System.getenv("database"));
			System.out.println(System.getenv("username"));
			LOGGER.info(System.getenv("username"));
			System.out.println(System.getenv("password"));
			LOGGER.info(System.getenv("password"));

			SpringApplication.run(DemoApplication.class, args);
		}

	// for WAR
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		 return builder.sources(DemoApplication.class);
	}
}
