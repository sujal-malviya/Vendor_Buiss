package com.vendorhub.vendor_onboarding.dto;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import jakarta.validation.constraints.NotBlank;

public record PaymentPlanRequest(
        @NotBlank(message = "advance payment is required") String advancePayment,
        @NotBlank(message = "pre payment is required") String prePayment,
        @NotBlank(message = "post payment is required") String postPayment) {

    public void applyTo(VendorPaymentPlan entity)
    {
        entity.setAdvancePayment(advancePayment);
        entity.setPrePayment(prePayment);
        entity.setPostPayment(postPayment);
    }

    public void patch(VendorPaymentPlan entity)
    {
        if (advancePayment != null) entity.setAdvancePayment(advancePayment);
        if (prePayment != null) entity.setPrePayment(prePayment);
        if (postPayment != null) entity.setPostPayment(postPayment);
    }
}
