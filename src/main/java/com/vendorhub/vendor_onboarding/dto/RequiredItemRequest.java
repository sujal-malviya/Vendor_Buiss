package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import jakarta.validation.constraints.NotBlank;

public record RequiredItemRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        String unit,
        Boolean active) {

    public void applyTo(RequiredItem entity)
    {
        entity.setName(name);
        entity.setDescription(description);
        entity.setUnit(unit);
        entity.setActive(active);
    }

    public void patch(RequiredItem entity)
    {
        if (name != null) entity.setName(name);
        if (description != null) entity.setDescription(description);
        if (unit != null) entity.setUnit(unit);
        if (active != null) entity.setActive(active);
    }
}
