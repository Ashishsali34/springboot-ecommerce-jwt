package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{



    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl (CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalpages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb != null){
            throw new APIException("Category with the name "+ categoryDTO.getCategoryName() + " already exists !!!");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setCategoryName(categoryDTO.getCategoryName());
        Category updated = categoryRepository.save(category);
        return modelMapper.map(updated, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(String id) {
        Category savedCategory= categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));
        categoryRepository.delete(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
