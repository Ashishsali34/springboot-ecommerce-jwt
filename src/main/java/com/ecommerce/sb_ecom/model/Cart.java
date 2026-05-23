package com.ecommerce.sb_ecom.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {

    @Id
    private String id;
    private String productId;
    private String cartId;
    private User user;
    private List<CartItem> cartItems = new ArrayList<>();
    private double totalPrice= 0.0;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
