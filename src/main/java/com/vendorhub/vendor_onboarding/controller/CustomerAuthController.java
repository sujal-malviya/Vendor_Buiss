package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.AuthResponse;
import com.vendorhub.vendor_onboarding.dto.CustomerLoginRequest;
import com.vendorhub.vendor_onboarding.dto.CustomerRegisterRequest;
import com.vendorhub.vendor_onboarding.dto.CustomerResponse;
import com.vendorhub.vendor_onboarding.service.CustomerAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;

    CustomerAuthController(CustomerAuthService customerAuthService)
    {
        this.customerAuthService = customerAuthService;
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody CustomerRegisterRequest request)
    {
        return customerAuthService.register(request);
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@Valid @RequestBody CustomerLoginRequest request)
    {
        return customerAuthService.login(request);
    }

    // Only customer tokens reach this (SecurityConfig), so "sub" is a customer id here
    @GetMapping("/me")
    public CustomerResponse me(@AuthenticationPrincipal Jwt jwt)
    {
        return customerAuthService.getCustomer(Long.valueOf(jwt.getSubject()));
    }
}
