package com.vendorhub.vendor_onboarding.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The client sends plain ids ("packageId": 3) instead of nested objects ({"menuPackage": {"id": 3}}).
 * The service loads the real package and dish, and checks the package belongs to the logged-in vendor.
 */
public record PackageDishRequest(
        @NotNull(message = "packageId is required") Long packageId,
        @NotNull(message = "dishId is required") Long dishId,
        @Positive(message = "quantity must be more than 0") Integer quantity) {
}
