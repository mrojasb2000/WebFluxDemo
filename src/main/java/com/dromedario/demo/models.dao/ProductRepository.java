package com.dromedario.demo.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.dromedario.demo.models.documents.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
