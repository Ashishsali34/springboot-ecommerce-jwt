package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {
    Optional<Category> findById(String id);
    Category deleteById(Integer id);
    Category findByCategoryName(@NotBlank @Size(min = 5, message = "Category must contain at least 5 character") String categoryName);
}
