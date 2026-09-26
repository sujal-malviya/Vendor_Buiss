package com.vendorhub.vendor_onboarding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Same rules as vendor registration (RegisterRequest), plus a name
public record CustomerRegisterRequest(
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "email is required") @Email(message = "email is not valid") String email,
        @NotBlank(message = "phone number is required")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone must be 10 to 15 digits") String phoneNumber,
        @NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password) {
}
