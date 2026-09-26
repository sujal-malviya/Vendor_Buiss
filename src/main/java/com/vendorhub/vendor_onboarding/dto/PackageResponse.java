package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;

import java.math.BigDecimal;

// No "vendor" field: the vendor account (with its password hash) can never end up in the JSON
public record PackageResponse(Long id, String name, String description, BigDecimal price, String priceUnit, Boolean active) {

    public static PackageResponse from(MenuPackage entity)
    {
        return new PackageResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getPriceUnit(),
                entity.getActive());
    }
}
