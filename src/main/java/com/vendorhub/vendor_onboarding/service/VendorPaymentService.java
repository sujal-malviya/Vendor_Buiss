package com.vendorhub.vendor_onboarding.service;

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

    public VendorPaymentPlan createPaymentPlan(VendorPaymentPlan paymentPlan)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorPaymentRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment plan already exists. Use PUT or PATCH to change it.");
        }
        paymentPlan.setId(null);
        paymentPlan.setVendorProfile(profile);
        return vendorPaymentRepository.save(paymentPlan);
    }

    public List<VendorPaymentPlan> getMyPaymentPlans()
    {
        return vendorPaymentRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    public VendorPaymentPlan getPaymentPlanById(Long id)
    {
        return vendorPaymentRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Payment plan", id));
    }

    public void deletePaymentPlan(Long id)
    {
        vendorPaymentRepository.delete(getPaymentPlanById(id));
    }

    public VendorPaymentPlan updatePaymentPlan(Long id, VendorPaymentPlan paymentPlan)
    {
        VendorPaymentPlan existing = getPaymentPlanById(id);
        paymentPlan.setId(existing.getId());
        paymentPlan.setVendorProfile(existing.getVendorProfile());
        return vendorPaymentRepository.save(paymentPlan);
    }

    public VendorPaymentPlan patchPaymentPlan(Long id, VendorPaymentPlan paymentPlan)
    {
        VendorPaymentPlan existing = getPaymentPlanById(id);
        if (paymentPlan.getPrePayment() != null) existing.setPrePayment(paymentPlan.getPrePayment());
        if (paymentPlan.getPostPayment() != null) existing.setPostPayment(paymentPlan.getPostPayment());
        if (paymentPlan.getAdvancePayment() != null) existing.setAdvancePayment(paymentPlan.getAdvancePayment());
        return vendorPaymentRepository.save(existing);
    }
}
