package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.EventType;

public record EventTypeResponse(Long id, String name, String description, Boolean active) {

    public static EventTypeResponse from(EventType entity)
    {
        return new EventTypeResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getActive());
    }
}
