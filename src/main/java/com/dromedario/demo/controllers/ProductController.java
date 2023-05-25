package com.dromedario.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.dromedario.demo.models.documents.Product;
import com.dromedario.demo.models.services.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@SessionAttributes("product")
@Controller
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping({ "/list", "/" })
    public String list(Model model) {
        Flux<Product> products = productService.findAllWithPropertyNameUpperCase();
        products.subscribe(product -> log.info(product.getName()));
        model.addAttribute("products", products);
        model.addAttribute("title", "Product List");
        return "list";
    }

    @GetMapping({ "/list-datadriver" })
    public String listDataDriven(Model model) {
        Flux<Product> products = productService.findAllWithPropertyNameUpperCase()
                .delayElements(java.time.Duration.ofSeconds(1));
        products.subscribe(product -> log.info(product.getName()));
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "Product List");
        return "list";
    }

    @GetMapping({ "/list-full" })
    public String listFull(Model model) {
        Flux<Product> products = productService.findAllWithPropertyNameUpperCase().repeat(5_000);
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "Product List");
        return "list";
    }

    @GetMapping({ "/list-chunked" })
    public String listChunked(Model model) {
        Flux<Product> products = productService.findAllWithPropertyNameUpperCase().repeat(5_000);
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "Product List");
        return "list-chunked";
    }

    @GetMapping("/form")
    public Mono<String> createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "product form");
        return Mono.just("form");
    }

    @GetMapping("/form/{id}")
    public Mono<String> editForm(@PathVariable String id, Model model) {
        return productService.findById(id)
                .doOnNext(product -> {
                    log.info("Product id: " + product.getId());
                    model.addAttribute("title", "Product Edit");
                    model.addAttribute("product", product);
                }).defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("product not exists"));
                    }
                    return Mono.just(p);
                })
                .then(Mono.just("form"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=notFound"));
    }

    @PostMapping(value = "/form")
    public Mono<String> saveForm(Product product, SessionStatus status) {
        status.setComplete();
        return productService.save(product)
                .doOnNext(p -> {
                    log.info("Product id: " + p.getId());
                })
                .thenReturn("redirect:/list");
    }
}
