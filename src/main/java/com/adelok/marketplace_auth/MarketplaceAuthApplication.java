package com.adelok.marketplace_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MarketplaceAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceAuthApplication.class, args);
	}

}
