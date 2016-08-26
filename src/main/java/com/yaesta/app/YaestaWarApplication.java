package com.yaesta.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.yaesta"})
@Configuration
public class YaestaWarApplication extends VitexSoapClient {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 5610283017020108090L;

	public static void main(String[] args) {
		SpringApplication.run(YaestaWarApplication.class, args);
	}
}
