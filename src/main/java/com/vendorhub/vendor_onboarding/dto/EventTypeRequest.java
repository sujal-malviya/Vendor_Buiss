package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.EventType;
import jakarta.validation.constraints.NotBlank;

public record EventTypeRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        Boolean active) {

    public void applyTo(EventType entity)
    {
        entity.setName(name);
        entity.setDescription(description);
        entity.setActive(active);
    }

    public void patch(EventType entity)
    {
        if (name != null) entity.setName(name);
        if (description != null) entity.setDescription(description);
        if (active != null) entity.setActive(active);
    }
}
