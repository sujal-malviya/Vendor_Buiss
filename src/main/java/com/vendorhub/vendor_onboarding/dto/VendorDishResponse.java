package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorDish;

import java.math.BigDecimal;

public record VendorDishResponse(Long id, Long dishId, String dishName, BigDecimal price, Boolean available) {

    public static VendorDishResponse from(VendorDish entity)
    {
        return new VendorDishResponse(
                entity.getId(),
                entity.getDish().getId(),
                entity.getDish().getName(),
                entity.getPrice(),
                entity.getAvailable());
    }
}
