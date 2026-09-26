package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BusinessInfoRequest(
        @NotBlank(message = "business name is required") String name,
        @NotBlank(message = "contact details are required") String contactDetail,
        @NotBlank(message = "address is required") String address,
        @NotBlank(message = "GST number is required") String gstNumber,
        @NotBlank(message = "FSSAI number is required") String fssaiNumber,
        @NotNull(message = "years in business is required")
        @PositiveOrZero(message = "years in business cannot be negative") Integer yearsInBusiness) {

    public void applyTo(VendorBusinessInfo entity)
    {
        entity.setName(name);
        entity.setContactDetail(contactDetail);
        entity.setAddress(address);
        entity.setGstNumber(gstNumber);
        entity.setFssaiNumber(fssaiNumber);
        entity.setYearsInBusiness(yearsInBusiness);
    }

    public void patch(VendorBusinessInfo entity)
    {
        if (name != null) entity.setName(name);
        if (contactDetail != null) entity.setContactDetail(contactDetail);
        if (address != null) entity.setAddress(address);
        if (gstNumber != null) entity.setGstNumber(gstNumber);
        if (fssaiNumber != null) entity.setFssaiNumber(fssaiNumber);
        if (yearsInBusiness != null) entity.setYearsInBusiness(yearsInBusiness);
    }
}
