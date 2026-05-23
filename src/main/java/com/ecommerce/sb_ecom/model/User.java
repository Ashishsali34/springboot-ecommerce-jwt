package com.ecommerce.sb_ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
@CompoundIndexes({
        @CompoundIndex(name = "username_unique_idx", def = "{ 'username': 1 }", unique = true),
        @CompoundIndex(name = "email_unique_idx", def = "{ 'email': 1 }", unique = true)
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public static final String SEQUENCE_NAME = "user_sequence";

    @Id
    private String id;

    private Long userNumber;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ToString.Exclude
    @JsonIgnore
    @DBRef
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @JsonIgnore
    @DBRef
    private List<Address> address = new ArrayList<>();

    @ToString.Exclude
    private Cart cart;

    @ToString.Exclude
    @DBRef
    private Set<Product> products;

    public User(String id,String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
