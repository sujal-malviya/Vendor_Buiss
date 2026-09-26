package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;

public record BusinessInfoResponse(
        Long id,
        String name,
        String contactDetail,
        String address,
        String gstNumber,
        String fssaiNumber,
        Integer yearsInBusiness) {

    public static BusinessInfoResponse from(VendorBusinessInfo entity)
    {
        return new BusinessInfoResponse(
                entity.getId(),
                entity.getName(),
                entity.getContactDetail(),
                entity.getAddress(),
                entity.getGstNumber(),
                entity.getFssaiNumber(),
                entity.getYearsInBusiness());
    }
}
