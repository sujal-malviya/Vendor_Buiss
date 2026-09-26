package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.PaymentPlanRequest;
import com.vendorhub.vendor_onboarding.dto.PaymentPlanResponse;
import com.vendorhub.vendor_onboarding.service.VendorPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/payment-plan")
public class VendorPaymentController {

    private final VendorPaymentService vendorPaymentService;

    VendorPaymentController(VendorPaymentService vendorPaymentService)
    {
        this.vendorPaymentService = vendorPaymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentPlanResponse createPaymentPlan(@Valid @RequestBody PaymentPlanRequest request)
    {
        return vendorPaymentService.createPaymentPlan(request);
    }

    @GetMapping
    public List<PaymentPlanResponse> getMyPaymentPlans()
    {
        return vendorPaymentService.getMyPaymentPlans();
    }

    @GetMapping("/{id}")
    public PaymentPlanResponse getPaymentPlanById(@PathVariable Long id)
    {
        return vendorPaymentService.getPaymentPlanById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePaymentPlan(@PathVariable Long id)
    {
        vendorPaymentService.deletePaymentPlan(id);
    }

    @PutMapping("/{id}")
    public PaymentPlanResponse updatePaymentPlan(@PathVariable Long id, @Valid @RequestBody PaymentPlanRequest request)
    {
        return vendorPaymentService.updatePaymentPlan(id, request);
    }

    @PatchMapping("/{id}")
    public PaymentPlanResponse patchPaymentPlan(@PathVariable Long id, @RequestBody PaymentPlanRequest request)
    {
        return vendorPaymentService.patchPaymentPlan(id, request);
    }
}
