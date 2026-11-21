package com.eic.product_catalogue_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message = "Name is required")
                              String name,
                              @NotBlank(message = "Email is required")
                              @Email(message = "Email format is invalid")
                              String email,
                              @NotBlank(message = "Password is required")
                              @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters")
                              String password)
{
}
