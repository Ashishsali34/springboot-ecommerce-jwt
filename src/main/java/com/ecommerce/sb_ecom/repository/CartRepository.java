package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    // Find the cart owned by a user (by email)
    Cart findCartByUserEmail(String email);

    // Find cart by user email and cart id
    Cart findCartByUserEmailAndId(String emailId, String id);

    // Find carts that contain a CartItem whose productId field equals the given id
    // Matches documents where cartItems.productId == productId
    List<Cart> findByCartItemsProductId(String productId);

    // If you use DBRef and want to match Product._id inside the DBRef, use:
    // Matches documents where cartItems.product.$id == productId (Spring maps via property path)
    List<Cart> findByCartItemsProduct_Id(String productId);

    List<Cart> findCartsByProductId(String id);

    // If you prefer an explicit query instead of derived method names, use:
    // @Query("{ 'cartItems.productId' : ?0 }")
    // List<Cart> findCartsContainingProductId(String productId);
}