package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import com.vendorhub.vendor_onboarding.repository.VendorPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class VendorPaymentService {
    private VendorPaymentRepository vendorPaymentRepository;

    VendorPaymentService(VendorPaymentRepository vendorPaymentRepository)
    {
        this.vendorPaymentRepository = vendorPaymentRepository;
    }

    public VendorPaymentPlan createVendorPayment(VendorPaymentPlan vendorPaymentPlan)
    {
        return vendorPaymentRepository.save(vendorPaymentPlan);
    }


    public List<VendorPaymentPlan> getAllVendorPayment()
    {

        return vendorPaymentRepository.findAll();
    }


    public VendorPaymentPlan getVendorPaymentById( Long id)
    {
        return vendorPaymentRepository.findById(id).orElseThrow(()->new RuntimeException("id not found : "+id));
    }

    public void deleteVendorPayment(Long id)
    {
        vendorPaymentRepository.deleteById(id);
    }


    public VendorPaymentPlan updateVendorPayment(Long id, VendorPaymentPlan vendorPaymentPlan)
    {
        if(!vendorPaymentRepository.existsById(id))
        {
            throw new RuntimeException("id not found : "+id);
        }
        vendorPaymentPlan.setId(id);
        return vendorPaymentRepository.save(vendorPaymentPlan);
    }


    public VendorPaymentPlan updatedVendorPayments( Long id ,  VendorPaymentPlan vendorPaymentPlan)
    {
        return vendorPaymentRepository.findById(id).map(existingId->{
            existingId.setId(vendorPaymentPlan.getId());
            existingId.setPrePayement(vendorPaymentPlan.getPrePayement());
            existingId.setPostPayment(vendorPaymentPlan.getPostPayment());
            existingId.setAdvancePayment(vendorPaymentPlan.getAdvancePayment());

            return vendorPaymentRepository.save(existingId);
        }).orElseThrow(()->new RuntimeException("id not found : "+id));
    }
}
