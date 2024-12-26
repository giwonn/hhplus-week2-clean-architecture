package com.example.hhplus;

import org.springframework.boot.SpringApplication;

public class TestHhplusApplication {

	public static void main(String[] args) {
		SpringApplication.from(HhplusApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
