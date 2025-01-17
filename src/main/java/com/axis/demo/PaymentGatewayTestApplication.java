package com.axis.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PaymentGatewayTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentGatewayTestApplication.class, args);
	}

}
