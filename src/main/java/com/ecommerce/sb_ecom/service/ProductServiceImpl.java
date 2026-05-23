package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repository.CartRepository;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import com.ecommerce.sb_ecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl (ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(String id, ProductDTO productDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", id));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        if(products == null){
            products = new ArrayList<>();
            category.setProducts(products);
        }
        products.removeIf(Objects::isNull);
        for (Product value : products) {
            if (value != null && value.getProductName() != null && value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            products.add(savedProduct);
            categoryRepository.save(category);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exist!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProduct = productRepository.findAll(pageDetails);

        List<Product> products = pageProduct.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        if(products.isEmpty()) {
            throw new APIException("No Product Exist!!!");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElement(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(String id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category","id",id));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProduct = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
        List<Product> products = pageProduct.getContent();

        if(products.isEmpty()){
            throw new APIException(category.getCategoryName() + "category does not have any products");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElement(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProduct = productRepository.findByProductNameLikeIgnoreCase(keyword, pageDetails);
        List<Product> products = pageProduct.getContent();



        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.size() == 0){
            throw new APIException("Products not found with keyword: " + keyword);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalElement(pageProduct.getTotalElements());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setLastPage(pageProduct.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String id) {
        Product productsFromDb = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id" ,id));
        Product product = modelMapper.map(productDTO, Product.class);
        productsFromDb.setProductName(product.getProductName());
        productsFromDb.setDescription(product.getDescription());
        productsFromDb.setQuantity(product.getQuantity());
        productsFromDb.setDiscount(product.getDiscount());
        productsFromDb.setPrice(product.getPrice());
        productsFromDb.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct = productRepository.save(productsFromDb);

        List<Cart> carts = cartRepository.findCartsByProductId(id);
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());
        cartDTOs.forEach(cart ->cartService.updateProductInCarts(cart.getId(), id));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id" ,id));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(String id, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "id" ,id));
        String fileName = fileService.uploadImage(System.getProperty("user.dir") + path, image);
        productFromDb.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }




}
