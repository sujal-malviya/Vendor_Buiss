package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;

public record MediaResponse(Long id, String image, String video) {

    public static MediaResponse from(VendorMedia entity)
    {
        return new MediaResponse(entity.getId(), entity.getImage(), entity.getVideo());
    }
}
