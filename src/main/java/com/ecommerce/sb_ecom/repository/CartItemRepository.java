package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
   // @Query("{ 'cart.id': ?0, 'product.id': ?1 }")
   // @Modifing
   CartItem findByCart_IdAndProduct_Id(String cartId, String productId);
   List<CartItem> findByCart_Id(String cartId);
}
