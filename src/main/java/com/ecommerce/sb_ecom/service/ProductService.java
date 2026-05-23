package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDTO addProduct(String id, ProductDTO productDTO) ;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(String id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, String id);

    ProductDTO deleteProduct(String id);

    ProductDTO updateProductImage(String id, MultipartFile image) throws IOException;
}
