package com.ecommerce.sb_ecom.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    public static final String SEQUENCE_NAME = "role_sequence";

    @Id
    private Long id;

    @Field("role_name")
    @Size(max = 20)
    @ToString.Exclude
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

    public @Size(max = 20) AppRole getRoleName() {
        return roleName;
    }
}
