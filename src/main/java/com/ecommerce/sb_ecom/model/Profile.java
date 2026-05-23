package com.ecommerce.sb_ecom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profile")
public class Profile {

    @Id
    private String id;

    @DBRef
    private User user;
}
