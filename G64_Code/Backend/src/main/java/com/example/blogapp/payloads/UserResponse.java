package com.example.blogapp.payloads;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserResponse {

    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty
    @Size(min = 10, max = 100)
    private String about;

    private Set<RoleDto> roles = new HashSet<>();

}
