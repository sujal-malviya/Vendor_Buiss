package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;

public record PaymentPlanResponse(Long id, String advancePayment, String prePayment, String postPayment) {

    public static PaymentPlanResponse from(VendorPaymentPlan entity)
    {
        return new PaymentPlanResponse(
                entity.getId(),
                entity.getAdvancePayment(),
                entity.getPrePayment(),
                entity.getPostPayment());
    }
}
