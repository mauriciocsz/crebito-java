package com.mauriciocsz.crebito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mauriciocsz.crebito")
public class CrebitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrebitoApplication.class, args);
	}
}
