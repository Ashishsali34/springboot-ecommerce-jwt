package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK); // Better than returning null
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO savedcategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedcategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable String id) {
        CategoryDTO deleteCategory = categoryService.deleteCategory(id);
        return new ResponseEntity<>(deleteCategory, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                                      @PathVariable String id) {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO,id);
         return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}
