package com.novent.foodordering.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.novent.foodordering.DemoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationTests.class);
//	System.out.println("Demo app appli.prop");
//	LOGGER.info("Demo app appli.prop");
//	System.out.println(System.getenv("database"));
//	LOGGER.info(System.getenv("database"));
//	System.out.println(System.getenv("username"));
//	LOGGER.info(System.getenv("username"));
//	System.out.println(System.getenv("password"));
//	LOGGER.info(System.getenv("password"));
	
	@Test
	public void contextLoads() {
		System.out.println("Demo app appli.prop");
		LOGGER.info("Demo app appli.prop");
		System.out.println(System.getenv("database"));
		LOGGER.info(System.getenv("database"));
		System.out.println(System.getenv("username"));
		LOGGER.info(System.getenv("username"));
		System.out.println(System.getenv("password"));
		LOGGER.info(System.getenv("password"));
	}
	}
