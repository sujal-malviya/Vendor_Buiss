package com.vendorhub.vendor_onboarding.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record VendorDishRequest(
        @NotNull(message = "dishId is required") Long dishId,
        @PositiveOrZero(message = "price cannot be negative") BigDecimal price,
        Boolean available) {
}
