package com.dromedario.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.dromedario.demo.models.dao.ProductRepository;
import com.dromedario.demo.models.documents.Product;

import ch.qos.logback.core.util.Duration;
import reactor.core.publisher.Flux;

@Controller
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository productRespository;

    @GetMapping({ "/list", "/" })
    public String list(Model model) {
        Flux<Product> products = productRespository.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                });
        products.subscribe(product -> log.info(product.getName()));
        model.addAttribute("products", products);
        model.addAttribute("title", "Product List");
        return "list";
    }

    @GetMapping({ "/list-datadriver" })
    public String listDataDriven(Model model) {
        Flux<Product> products = productRespository.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }).delayElements(java.time.Duration.ofSeconds(1));
        products.subscribe(product -> log.info(product.getName()));
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "Product List");
        return "list";
    }

    @GetMapping({ "/list-full" })
    public String listFull(Model model) {
        Flux<Product> products = productRespository.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }).repeat(5_000);
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "Product List");
        return "list";
    }
}
