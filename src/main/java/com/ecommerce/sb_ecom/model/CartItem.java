package com.ecommerce.sb_ecom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem {

    @Id
    private String id;

    @DBRef
    private Cart cart;

    @DBRef
    private Product product;

    @Indexed
    private String cartId;

    @Indexed
    private String productId;

    private Integer quantity;
    private double discount;
    private double productPrice;
}