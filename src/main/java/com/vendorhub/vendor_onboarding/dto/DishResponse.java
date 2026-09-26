package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.Dish;

public record DishResponse(
        Long id,
        String name,
        String description,
        String category,
        String cuisine,
        Boolean vegetarian,
        Boolean active) {

    public static DishResponse from(Dish entity)
    {
        return new DishResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getCuisine(),
                entity.getVegetarian(),
                entity.getActive());
    }
}
