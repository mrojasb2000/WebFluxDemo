package com.dromedario.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dromedario.demo.models.dao.ProductRepository;
import com.dromedario.demo.models.documents.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    private static Logger log = LoggerFactory.getLogger(ProductRestController.class);

    @GetMapping()
    public Flux<Product> index() {
        Flux<Product> products = productRepository.findAll()
                .doOnNext(product -> log.info(product.getName()));
        return products;
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable String id) {
        return productRepository.findById(id);
    }
}
