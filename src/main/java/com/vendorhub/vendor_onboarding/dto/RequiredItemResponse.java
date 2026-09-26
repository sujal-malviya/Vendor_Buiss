package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;

public record RequiredItemResponse(Long id, String name, String description, String unit, Boolean active) {

    public static RequiredItemResponse from(RequiredItem entity)
    {
        return new RequiredItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getUnit(),
                entity.getActive());
    }
}
