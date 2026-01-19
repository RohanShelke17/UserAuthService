package com.project.authify.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Name should be not empty")
    private String name;
    @Email(message = "Enter vaild email")
    @NotNull(message = "Email should be not empty")
    private String email;
    @Size(min = 6,message = "Password must be atleast 6 charatcter")
    private String password;
}
