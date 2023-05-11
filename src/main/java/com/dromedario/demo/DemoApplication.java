package com.dromedario.demo;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		log.info("Before callback");
		intervalOperatorFromCreateExample();
		log.info("After callback");
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
					if (count == 3) {
						emitter.error(new InterruptedException("Error: count equals 3"));
					}
				}

			}, 1_000, 1_000);
		})
				.subscribe(next -> log.info(next.toString()),
						err -> log.error(err.getMessage()),
						() -> log.info("Flux completed"));
	}
}
