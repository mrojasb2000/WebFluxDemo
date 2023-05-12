package com.dromedario.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.dromedario.demo.models.dao.ProductRepository;
import com.dromedario.demo.models.documents.Product;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		log.info("Before callback");
		loadDataProductStream();
		log.info("After callback");
	}

	private void loadDataProductStream() {
		productRepository.deleteAll()
				.subscribe();
		Flux.just(Product.builder().name("Cort Action PJ Bass Guitar").price(10_000.50).build(),
				Product.builder().name("Cort Action Bass V plus Bass Guitar").price(10_000.50).build(),
				Product.builder().name("Epic Electro Acustic Bass Guitar").price(5_000.75).build(),
				Product.builder().name("ESP LTD 4 Strings Bass Guitar").price(10_000.50).build(),
				Product.builder().name("ESP LTD 5 Strings Bass Guitar").price(10_000.50).build(),
				Product.builder().name("EPIC Electro acustic Guitar").price(8_000.50).build(),
				Product.builder().name("EPIC Telecaster Guitar").price(12_000.50).build())
				.flatMap(product -> productRepository.save(
						product))
				.subscribe(product -> log.info(product.getId() + " - " + product.getName()));
	}
}
