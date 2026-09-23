package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
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
    public VendorPaymentPlan createPaymentPlan(@Valid @RequestBody VendorPaymentPlan paymentPlan)
    {
        return vendorPaymentService.createPaymentPlan(paymentPlan);
    }

    @GetMapping
    public List<VendorPaymentPlan> getMyPaymentPlans()
    {
        return vendorPaymentService.getMyPaymentPlans();
    }

    @GetMapping("/{id}")
    public VendorPaymentPlan getPaymentPlanById(@PathVariable Long id)
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
    public VendorPaymentPlan updatePaymentPlan(@PathVariable Long id, @Valid @RequestBody VendorPaymentPlan paymentPlan)
    {
        return vendorPaymentService.updatePaymentPlan(id, paymentPlan);
    }

    @PatchMapping("/{id}")
    public VendorPaymentPlan patchPaymentPlan(@PathVariable Long id, @RequestBody VendorPaymentPlan paymentPlan)
    {
        return vendorPaymentService.patchPaymentPlan(id, paymentPlan);
    }
}
