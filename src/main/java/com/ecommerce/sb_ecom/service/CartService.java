package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.payload.CartDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CartService {

    CartDTO addProductToCart(String id, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, String CartId);

    @Transactional
    CartDTO updateProductQuantityInCart(String productId, Integer quantity);

    String deleteProductFromCart(String Id, String productId);

    void updateProductInCarts(String id, String id1);
}
