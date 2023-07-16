package com.example.blogapp.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserDto {

    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3)
    private String name;

    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Password length must be from 3-10 characters")
    private String password;

    @NotEmpty
    private String matchingPassword;

    @NotEmpty
    @Size(min = 10, max = 100)
    private String about;
    private Set<RoleDto> roles = new HashSet<>();

//    @JsonIgnore
//    public String getMatchingPassword() {
//        return this.matchingPassword;
//    }
//
//
//    @JsonIgnore
//    public String getPassword() {
//        return this.password;
//    }

}
