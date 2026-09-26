package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * businessInfo is optional and only used by POST (create the profile and its business info in one call).
 * PUT and PATCH only change name and address; business info has its own endpoint.
 */
public record VendorProfileRequest(
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "address is required") String address,
        @Valid BusinessInfoRequest businessInfo) {

    public void applyTo(VendorProfile entity)
    {
        entity.setName(name);
        entity.setAddress(address);
    }

    public void patch(VendorProfile entity)
    {
        if (name != null) entity.setName(name);
        if (address != null) entity.setAddress(address);
    }
}
