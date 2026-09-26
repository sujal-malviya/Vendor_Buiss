package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.Customer;

// No password field, so the hash can never be sent to a client
public record CustomerResponse(Long id, String name, String email, String phoneNumber) {

    public static CustomerResponse from(Customer entity)
    {
        return new CustomerResponse(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhoneNumber());
    }
}
