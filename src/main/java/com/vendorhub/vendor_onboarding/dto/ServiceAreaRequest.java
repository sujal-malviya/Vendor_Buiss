package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceAreaRequest(
        @NotNull(message = "max people is required")
        @Max(value = 1000, message = "maximum that can be served is 1000") Long maxPeople,
        @NotBlank(message = "service PIN is required") String servicePin,
        @NotBlank(message = "service city is required") String serviceCity,
        @NotBlank(message = "service country is required") String serviceCountry,
        @NotNull(message = "order size is required") Long orderSize,
        @NotNull(message = "minimum order size is required")
        @Min(value = 200, message = "minimum order size must be at least 200") Long minimumOrderSize,
        @NotNull(message = "maximum order size is required")
        @Max(value = 1000, message = "maximum order size cannot be more than 1000") Long maximumOrderSize,
        @NotNull(message = "minimum order value is required")
        @Min(value = 250, message = "minimum order value must be at least 250 per plate") Long minOrderValue) {

    public void applyTo(VendorServiceArea entity)
    {
        entity.setMaxPeople(maxPeople);
        entity.setServicePin(servicePin);
        entity.setServiceCity(serviceCity);
        entity.setServiceCountry(serviceCountry);
        entity.setOrderSize(orderSize);
        entity.setMinimumOrderSize(minimumOrderSize);
        entity.setMaximumOrderSize(maximumOrderSize);
        entity.setMinOrderValue(minOrderValue);
    }

    public void patch(VendorServiceArea entity)
    {
        if (maxPeople != null) entity.setMaxPeople(maxPeople);
        if (servicePin != null) entity.setServicePin(servicePin);
        if (serviceCity != null) entity.setServiceCity(serviceCity);
        if (serviceCountry != null) entity.setServiceCountry(serviceCountry);
        if (orderSize != null) entity.setOrderSize(orderSize);
        if (minimumOrderSize != null) entity.setMinimumOrderSize(minimumOrderSize);
        if (maximumOrderSize != null) entity.setMaximumOrderSize(maximumOrderSize);
        if (minOrderValue != null) entity.setMinOrderValue(minOrderValue);
    }
}
