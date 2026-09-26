package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.BankDetailRequest;
import com.vendorhub.vendor_onboarding.dto.BankDetailResponse;
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

    public BankDetailResponse createBankDetail(BankDetailRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorBankRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bank details already exist. Use PUT or PATCH to change them.");
        }
        VendorBankDetail bankDetail = new VendorBankDetail();   // request DTO -> new entity
        request.applyTo(bankDetail);
        bankDetail.setVendorProfile(profile);                   // the owner always comes from the token
        return BankDetailResponse.from(vendorBankRepository.save(bankDetail));   // entity -> response DTO
    }

    public List<BankDetailResponse> getMyBankDetails()
    {
        return vendorBankRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(BankDetailResponse::from)
                .toList();
    }

    public BankDetailResponse getBankDetailById(Long id)
    {
        return BankDetailResponse.from(findOwned(id));
    }

    public void deleteBankDetail(Long id)
    {
        vendorBankRepository.delete(findOwned(id));
    }

    public BankDetailResponse updateBankDetail(Long id, BankDetailRequest request)
    {
        VendorBankDetail existing = findOwned(id);
        request.applyTo(existing);
        return BankDetailResponse.from(vendorBankRepository.save(existing));
    }

    public BankDetailResponse patchBankDetail(Long id, BankDetailRequest request)
    {
        VendorBankDetail existing = findOwned(id);
        request.patch(existing);
        return BankDetailResponse.from(vendorBankRepository.save(existing));
    }

    // Looks the row up by id AND owner, so another vendor's id gives 404 instead of their data
    private VendorBankDetail findOwned(Long id)
    {
        return vendorBankRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Bank detail", id));
    }
}
