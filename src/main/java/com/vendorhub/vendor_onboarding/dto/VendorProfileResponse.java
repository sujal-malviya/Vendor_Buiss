package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;

public record VendorProfileResponse(Long id, String name, String address, BusinessInfoResponse businessInfo) {

    public static VendorProfileResponse from(VendorProfile entity)
    {
        return new VendorProfileResponse(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getBusinessInfo() == null ? null : BusinessInfoResponse.from(entity.getBusinessInfo()));
    }
}
