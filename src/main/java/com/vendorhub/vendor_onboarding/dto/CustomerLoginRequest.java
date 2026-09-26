package com.vendorhub.vendor_onboarding.dto;

import jakarta.validation.constraints.NotBlank;

// identifier = email or phone number, same as vendor login
public record CustomerLoginRequest(@NotBlank String identifier, @NotBlank String password) {
}
