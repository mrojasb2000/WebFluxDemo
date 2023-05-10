package com.dromedario.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println("Before callback");
		intervalOperatorFromCreateExample();
		System.out.println("After callback");
	}

	private void intervalOperatorFromCreateExample() {
		Flux.create(emitter -> {
			System.out.println("Hello from emitter");
		});
	}
}
