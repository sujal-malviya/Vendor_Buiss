package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorBankRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorBankService {

    private final VendorBankRepository vendorBankRepository;
    private final CurrentVendor currentVendor;

    VendorBankService(VendorBankRepository vendorBankRepository, CurrentVendor currentVendor)
    {
        this.vendorBankRepository = vendorBankRepository;
        this.currentVendor = currentVendor;
    }

    public VendorBankDetail createBankDetail(VendorBankDetail bankDetail)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorBankRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bank details already exist. Use PUT or PATCH to change them.");
        }
        bankDetail.setId(null);
        bankDetail.setVendorProfile(profile);
        return vendorBankRepository.save(bankDetail);
    }

    public List<VendorBankDetail> getMyBankDetails()
    {
        return vendorBankRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    // Looks the row up by id AND owner, so another vendor's id gives 404 instead of their data
    public VendorBankDetail getBankDetailById(Long id)
    {
        return vendorBankRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Bank detail", id));
    }

    public void deleteBankDetail(Long id)
    {
        vendorBankRepository.delete(getBankDetailById(id));
    }

    public VendorBankDetail updateBankDetail(Long id, VendorBankDetail bankDetail)
    {
        VendorBankDetail existing = getBankDetailById(id);
        bankDetail.setId(existing.getId());
        bankDetail.setVendorProfile(existing.getVendorProfile());
        return vendorBankRepository.save(bankDetail);
    }

    public VendorBankDetail patchBankDetail(Long id, VendorBankDetail bankDetail)
    {
        VendorBankDetail existing = getBankDetailById(id);
        if (bankDetail.getAccountNumber() != null) existing.setAccountNumber(bankDetail.getAccountNumber());
        if (bankDetail.getAccountHolderName() != null) existing.setAccountHolderName(bankDetail.getAccountHolderName());
        if (bankDetail.getIfscCode() != null) existing.setIfscCode(bankDetail.getIfscCode());
        return vendorBankRepository.save(existing);
    }
}
