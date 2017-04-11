package com.yaesta.app;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
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
@EnableJSONDoc
public class YaestaWarApplication extends YaestaSoapClient {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 5610283017020108090L;

	public static void main(String[] args) {
		SpringApplication.run(YaestaWarApplication.class, args);
	}
}
