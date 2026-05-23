package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.service.CartService;
import com.ecommerce.sb_ecom.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartService cartService;

    @PostMapping("/carts/products/{id}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable String id,
                                                     @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(id, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String emailId = authUtil.loggedInEmail();

        // Find cart by user email
        Cart cart = cartRepository.findCartByUserEmail(emailId);

        // ✅ NEW: Throw exception if cart doesn't exist
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "user email", emailId);
        }

        String id = cart.getId();
        CartDTO cartDTO = cartService.getCart(emailId, id);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable String productId,
                                                     @PathVariable String operation) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{Id}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable String Id,
                                                        @PathVariable String productId) {
        String status = cartService.deleteProductFromCart(Id, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}