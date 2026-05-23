package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{id}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable String id){
        ProductDTO savedproductDTO =  productService.addProduct(id, productDTO);
        return new ResponseEntity<>(savedproductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{id}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable String id,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder

                                                                 ) {
        ProductResponse productResponse = productService.searchByCategory(id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder

                                                                ) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping ("/admin/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct (@Valid @RequestBody ProductDTO productDTO,
                                                     @PathVariable String id) {
        ProductDTO updateProductDTO = productService.updateProduct(productDTO,id);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);
    }

    @DeleteMapping ("/admin/products/{id}")
    public ResponseEntity<ProductDTO> deleteProduct (@PathVariable String id) {
       ProductDTO deletedProduct =  productService.deleteProduct(id);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping(value = "/products/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable String id,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updateProduct = productService.updateProductImage(id, image);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }
}
