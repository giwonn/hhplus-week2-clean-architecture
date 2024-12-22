package com.example.hhplus;

import org.springframework.boot.SpringApplication;

public class TestHhplusWeek2CleanArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.from(HhplusWeek2CleanArchitectureApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
