package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import jakarta.validation.constraints.NotBlank;

/**
 * What the client is allowed to SEND for bank details.
 * There is no id and no vendorProfile here, so the client simply cannot set them.
 *
 * POST and PUT use it with @Valid (all fields required).
 * PATCH uses it without @Valid, and only the fields that are not null are copied.
 */
public record BankDetailRequest(
        @NotBlank(message = "account number is required") String accountNumber,
        @NotBlank(message = "account holder name is required") String accountHolderName,
        @NotBlank(message = "IFSC code is required") String ifscCode) {

    // Used by POST and PUT: copy every field
    public void applyTo(VendorBankDetail entity)
    {
        entity.setAccountNumber(accountNumber);
        entity.setAccountHolderName(accountHolderName);
        entity.setIfscCode(ifscCode);
    }

    // Used by PATCH: copy only the fields the client sent
    public void patch(VendorBankDetail entity)
    {
        if (accountNumber != null) entity.setAccountNumber(accountNumber);
        if (accountHolderName != null) entity.setAccountHolderName(accountHolderName);
        if (ifscCode != null) entity.setIfscCode(ifscCode);
    }
}
