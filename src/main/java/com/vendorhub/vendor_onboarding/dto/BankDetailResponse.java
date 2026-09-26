package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;

/**
 * What the client GETS BACK for bank details.
 * The full account number is never sent back, only the last 4 digits.
 * That is only possible because the response is a separate class from the entity.
 */
public record BankDetailResponse(Long id, String accountHolderName, String maskedAccountNumber, String ifscCode) {

    public static BankDetailResponse from(VendorBankDetail entity)
    {
        return new BankDetailResponse(
                entity.getId(),
                entity.getAccountHolderName(),
                mask(entity.getAccountNumber()),
                entity.getIfscCode());
    }

    private static String mask(String accountNumber)
    {
        if (accountNumber == null || accountNumber.length() <= 4)
        {
            return "XXXX";
        }
        return "X".repeat(accountNumber.length() - 4) + accountNumber.substring(accountNumber.length() - 4);
    }
}
