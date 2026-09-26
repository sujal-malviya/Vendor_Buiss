package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.Dish;
import jakarta.validation.constraints.NotBlank;

public record DishRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        String category,
        String cuisine,
        Boolean vegetarian,
        Boolean active) {

    public void applyTo(Dish entity)
    {
        entity.setName(name);
        entity.setDescription(description);
        entity.setCategory(category);
        entity.setCuisine(cuisine);
        entity.setVegetarian(vegetarian);
        entity.setActive(active);
    }

    public void patch(Dish entity)
    {
        if (name != null) entity.setName(name);
        if (description != null) entity.setDescription(description);
        if (category != null) entity.setCategory(category);
        if (cuisine != null) entity.setCuisine(cuisine);
        if (vegetarian != null) entity.setVegetarian(vegetarian);
        if (active != null) entity.setActive(active);
    }
}
