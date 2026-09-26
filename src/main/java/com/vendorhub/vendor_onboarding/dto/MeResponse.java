package com.vendorhub.vendor_onboarding.dto;

import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

public record MeResponse(String vendorId, String identifier, String role, Instant tokenExpiresAt) {

    public static MeResponse from(Jwt jwt)
    {
        return new MeResponse(
                jwt.getSubject(),
                jwt.getClaimAsString("identifier"),
                jwt.getClaimAsString("scope"),
                jwt.getExpiresAt());
    }
}
