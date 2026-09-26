package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.PaymentPlanRequest;
import com.vendorhub.vendor_onboarding.dto.PaymentPlanResponse;
import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorPaymentRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorPaymentService {

    private final VendorPaymentRepository vendorPaymentRepository;
    private final CurrentVendor currentVendor;

    VendorPaymentService(VendorPaymentRepository vendorPaymentRepository, CurrentVendor currentVendor)
    {
        this.vendorPaymentRepository = vendorPaymentRepository;
        this.currentVendor = currentVendor;
    }

    public PaymentPlanResponse createPaymentPlan(PaymentPlanRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorPaymentRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment plan already exists. Use PUT or PATCH to change it.");
        }
        VendorPaymentPlan paymentPlan = new VendorPaymentPlan();
        request.applyTo(paymentPlan);
        paymentPlan.setVendorProfile(profile);
        return PaymentPlanResponse.from(vendorPaymentRepository.save(paymentPlan));
    }

    public List<PaymentPlanResponse> getMyPaymentPlans()
    {
        return vendorPaymentRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(PaymentPlanResponse::from)
                .toList();
    }

    public PaymentPlanResponse getPaymentPlanById(Long id)
    {
        return PaymentPlanResponse.from(findOwned(id));
    }

    public void deletePaymentPlan(Long id)
    {
        vendorPaymentRepository.delete(findOwned(id));
    }

    public PaymentPlanResponse updatePaymentPlan(Long id, PaymentPlanRequest request)
    {
        VendorPaymentPlan existing = findOwned(id);
        request.applyTo(existing);
        return PaymentPlanResponse.from(vendorPaymentRepository.save(existing));
    }

    public PaymentPlanResponse patchPaymentPlan(Long id, PaymentPlanRequest request)
    {
        VendorPaymentPlan existing = findOwned(id);
        request.patch(existing);
        return PaymentPlanResponse.from(vendorPaymentRepository.save(existing));
    }

    private VendorPaymentPlan findOwned(Long id)
    {
        return vendorPaymentRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Payment plan", id));
    }
}
