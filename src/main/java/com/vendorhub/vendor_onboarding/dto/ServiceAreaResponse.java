package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;

public record ServiceAreaResponse(
        Long id,
        Long maxPeople,
        String servicePin,
        String serviceCity,
        String serviceCountry,
        Long orderSize,
        Long minimumOrderSize,
        Long maximumOrderSize,
        Long minOrderValue) {

    public static ServiceAreaResponse from(VendorServiceArea entity)
    {
        return new ServiceAreaResponse(
                entity.getId(),
                entity.getMaxPeople(),
                entity.getServicePin(),
                entity.getServiceCity(),
                entity.getServiceCountry(),
                entity.getOrderSize(),
                entity.getMinimumOrderSize(),
                entity.getMaximumOrderSize(),
                entity.getMinOrderValue());
    }
}
