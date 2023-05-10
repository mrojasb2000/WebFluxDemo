package com.dromedario.demo;

import java.util.Timer;
import java.util.TimerTask;

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
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				private Integer count = 0;

				@Override
				public void run() {
					emitter.next(++count);
					if (count == 5) {
						timer.cancel();
						emitter.complete();
					}
				}

			}, 1_000, 1_000);
		})
				.doOnNext(value -> System.out.println("Running Timer Task..." + value.toString()))
				.doOnComplete(() -> System.out.println("Flux completed"))
				.subscribe();
	}
}
