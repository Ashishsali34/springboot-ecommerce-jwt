package com.ecommerce.sb_ecom.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    private String id;   // MongoDB _id field
    @NotBlank
    @Size(min = 3, message="Product name must contain atleast 3 character")
    private String productName;

    @NotBlank
    @Size(min = 6, message="Product description must contain atleast 6 character")
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    //@DBRef
    private Category category;

    //@DBRef
    private User user;

    //@DBRef
    private List<CartItem> products = new ArrayList<>();
}

