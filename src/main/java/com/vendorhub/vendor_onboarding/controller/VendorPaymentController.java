package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import com.vendorhub.vendor_onboarding.service.VendorPaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/payments")
public class VendorPaymentController {

    private VendorPaymentService vendorPaymentService;
    VendorPaymentController(VendorPaymentService vendorPaymentService)
    {
        this.vendorPaymentService =vendorPaymentService;
    }

    @PostMapping
    public VendorPaymentPlan createVendorPayment(@Valid @RequestBody VendorPaymentPlan vendorPaymentPlan)
    {
        return vendorPaymentService.createVendorPayment(vendorPaymentPlan);
    }

    @GetMapping
    public List<VendorPaymentPlan> getAllVendorPayment()
    {

        return vendorPaymentService.getAllVendorPayment();
    }

    @GetMapping("/{id}")
    public VendorPaymentPlan getVendorPaymentById(@PathVariable Long id)
    {
        return vendorPaymentService.getVendorPaymentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorPayment(@PathVariable Long id)
    {
        vendorPaymentService.deleteVendorPayment(id);
    }

    @PutMapping("/{id}")
    public VendorPaymentPlan updateVendorPayment(@PathVariable Long id,@RequestBody VendorPaymentPlan vendorPaymentPlan)
    {
        return vendorPaymentService.updateVendorPayment(id,vendorPaymentPlan);
    }

    @PatchMapping("/{id}")
    public VendorPaymentPlan updatedVendorPayments(@PathVariable Long id , @RequestBody VendorPaymentPlan vendorPaymentPlan)
    {
        return vendorPaymentService.updatedVendorPayments(id,vendorPaymentPlan);
    }
}
