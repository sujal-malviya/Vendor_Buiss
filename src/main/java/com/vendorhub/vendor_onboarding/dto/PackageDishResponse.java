package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.PackageDish;

// Flat and small: the ids plus the names a screen needs, instead of the whole package and dish objects
public record PackageDishResponse(Long id, Long packageId, String packageName, Long dishId, String dishName, Integer quantity) {

    public static PackageDishResponse from(PackageDish entity)
    {
        return new PackageDishResponse(
                entity.getId(),
                entity.getMenuPackage().getId(),
                entity.getMenuPackage().getName(),
                entity.getDish().getId(),
                entity.getDish().getName(),
                entity.getQuantity());
    }
}
