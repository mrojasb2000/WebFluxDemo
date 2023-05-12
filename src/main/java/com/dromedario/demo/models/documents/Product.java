package com.dromedario.demo.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "products")
@Getter
@Setter
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private Double price;
    private Date createAt;
}