package com.dromedario.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dromedario.demo.models.dao.ProductRepository;
import com.dromedario.demo.models.documents.Product;

import reactor.core.publisher.Flux;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRespository;

    @GetMapping({ "/list", "/" })
    public String list(Model model) {
        Flux<Product> products = productRespository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("title", "Product List");
        return "list";
    }
}
