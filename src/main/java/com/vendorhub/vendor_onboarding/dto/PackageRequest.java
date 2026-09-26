package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PackageRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        @PositiveOrZero(message = "price cannot be negative") BigDecimal price,
        String priceUnit,
        Boolean active) {

    public void applyTo(MenuPackage entity)
    {
        entity.setName(name);
        entity.setDescription(description);
        entity.setPrice(price);
        entity.setPriceUnit(priceUnit);
        entity.setActive(active);
    }

    public void patch(MenuPackage entity)
    {
        if (name != null) entity.setName(name);
        if (description != null) entity.setDescription(description);
        if (price != null) entity.setPrice(price);
        if (priceUnit != null) entity.setPriceUnit(priceUnit);
        if (active != null) entity.setActive(active);
    }
}
