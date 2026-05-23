package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.CartItem;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.repository.CartItemRepository;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.repository.ProductRepository;
import com.ecommerce.sb_ecom.util.AuthUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(String productId, Integer quantity) {

        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId));

        if (product.getQuantity() <= 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException(
                    "Please order quantity less than or equal to available stock: "
                            + product.getQuantity());
        }

        // Maintain cart items list
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if(cartItem != null) {
            // Product already in cart - just update quantity and price
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
        } else {
            // Create new Cart Item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem = cartItemRepository.save(cartItem);
            cart.getCartItems().add(cartItem);
        }

        // Update inventory and cart total
        double itemTotal = product.getSpecialPrice() * quantity;
        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + itemTotal);

        productRepository.save(product);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.size() == 0) {
            throw new APIException("No cart exists");
        }
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, String id) {
        Cart cart = cartRepository.findCartByUserEmailAndId(emailId, id);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart", "id", id);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(String productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByUserEmail(emailId);
        String cartId = userCart.getId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id", productId));

        if (product.getQuantity() <= 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException(
                    "Please order quantity less than or equal to available stock: " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
        if(cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        int currentQty = cartItem.getQuantity() == null ? 0 : cartItem.getQuantity();
        int newQty = currentQty + quantity;
        if (newQty < 0) {
            throw new APIException("Quantity cannot be negative");
        }

        if(newQty == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setQuantity(newQty);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cartItemRepository.findByCart_Id(cartId);
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
        if(cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        // Restore product quantity to inventory
        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);

        // Update cart total price
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        // Remove from cart items list
        cart.getCartItems().remove(cartItem);

        // Delete the cart item from repository
        cartItemRepository.deleteById(cartItem.getId());

        // Save the updated cart
        cartRepository.save(cart);

        return "Product " + product.getProductName() + " removed from the cart!!!";
    }

    @Override
    public void updateProductInCarts(String id, String id1) {
        
    }

    private Cart getOrCreateCart() {

        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByUserEmail(email);

        if (cart != null) {
            return cart;
        }

        Cart newCart = new Cart();
        newCart.setUser(authUtil.loggedInUser());
        newCart.setTotalPrice(0.0);
        newCart.setCartItems(new ArrayList<>());

        return cartRepository.save(newCart);
    }
}