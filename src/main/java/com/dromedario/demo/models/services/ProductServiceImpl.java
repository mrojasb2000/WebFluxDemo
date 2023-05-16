package com.dromedario.demo.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dromedario.demo.models.dao.ProductRepository;
import com.dromedario.demo.models.documents.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Flux<Product> findAll() {
        log.info("repository find all...");
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> findById(String id) {
        log.info("repository find by Id: " + id);
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        log.info("repository save entity...");
        return productRepository.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        log.info("repository dele by entity...");
        return productRepository.delete(product);
    }

    @Override
    public Flux<Product> findAllWithPropertyNameUpperCase() {
        Flux<Product> products = findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        });
        return products;
    }
}
