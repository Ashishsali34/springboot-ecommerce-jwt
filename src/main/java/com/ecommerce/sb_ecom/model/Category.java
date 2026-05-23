package com.ecommerce.sb_ecom.model;


import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;          // maps to MongoDB _id (ObjectId as String)

    @NotBlank
    @Size(min = 5, message = "Category must contain at least 5 character")
    @Field("category_name")
    private String categoryName;

    @DBRef(lazy = true)
    private List<Product> products = new ArrayList<>();

}
