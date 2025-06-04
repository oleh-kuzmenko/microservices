package com.ol.app.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class AppManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppManagerApplication.class, args);
	}

}
