package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.BusinessInfoRequest;
import com.vendorhub.vendor_onboarding.dto.BusinessInfoResponse;
import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorBusinessInfoRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorBusinessService {

    private final VendorBusinessInfoRepository vendorBusinessInfoRepository;
    private final CurrentVendor currentVendor;

    VendorBusinessService(VendorBusinessInfoRepository vendorBusinessInfoRepository, CurrentVendor currentVendor)
    {
        this.vendorBusinessInfoRepository = vendorBusinessInfoRepository;
        this.currentVendor = currentVendor;
    }

    public BusinessInfoResponse createBusinessInfo(BusinessInfoRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorBusinessInfoRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Business info already exists. Use PUT or PATCH to change it.");
        }
        VendorBusinessInfo businessInfo = new VendorBusinessInfo();
        request.applyTo(businessInfo);
        businessInfo.setVendorProfile(profile);
        return BusinessInfoResponse.from(vendorBusinessInfoRepository.save(businessInfo));
    }

    public List<BusinessInfoResponse> getMyBusinessInfo()
    {
        return vendorBusinessInfoRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(BusinessInfoResponse::from)
                .toList();
    }

    public BusinessInfoResponse getBusinessInfoById(Long id)
    {
        return BusinessInfoResponse.from(findOwned(id));
    }

    // One transaction, so the unlink below and the delete happen together on the same loaded objects
    @Transactional
    public void deleteBusinessInfo(Long id)
    {
        VendorBusinessInfo existing = findOwned(id);
        // The profile cascades ALL to its business info; unlink it first or saving the profile would bring it back
        existing.getVendorProfile().setBusinessInfo(null);
        vendorBusinessInfoRepository.delete(existing);
    }

    public BusinessInfoResponse updateBusinessInfo(Long id, BusinessInfoRequest request)
    {
        VendorBusinessInfo existing = findOwned(id);
        request.applyTo(existing);
        return BusinessInfoResponse.from(vendorBusinessInfoRepository.save(existing));
    }

    public BusinessInfoResponse patchBusinessInfo(Long id, BusinessInfoRequest request)
    {
        VendorBusinessInfo existing = findOwned(id);
        request.patch(existing);
        return BusinessInfoResponse.from(vendorBusinessInfoRepository.save(existing));
    }

    private VendorBusinessInfo findOwned(Long id)
    {
        return vendorBusinessInfoRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Business info", id));
    }
}
