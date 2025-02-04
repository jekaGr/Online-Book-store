package com.example.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequestDto {
    @NotBlank
    @Email
    @Size(min = 8, max = 25)
    private String email;
    @NotBlank
    @Size(min = 8, max = 25)
    private String password;
}
