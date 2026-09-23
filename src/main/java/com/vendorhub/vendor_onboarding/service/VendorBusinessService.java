package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorBusinessInfoRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    public VendorBusinessInfo createBusinessInfo(VendorBusinessInfo businessInfo)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorBusinessInfoRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Business info already exists. Use PUT or PATCH to change it.");
        }
        businessInfo.setId(null);
        businessInfo.setVendorProfile(profile);
        return vendorBusinessInfoRepository.save(businessInfo);
    }

    public List<VendorBusinessInfo> getMyBusinessInfo()
    {
        return vendorBusinessInfoRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    public VendorBusinessInfo getBusinessInfoById(Long id)
    {
        return vendorBusinessInfoRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Business info", id));
    }

    public void deleteBusinessInfo(Long id)
    {
        VendorBusinessInfo existing = getBusinessInfoById(id);
        // The profile cascades ALL to its business info; unlink it first or saving the profile would bring it back
        existing.getVendorProfile().setBusinessInfo(null);
        vendorBusinessInfoRepository.delete(existing);
    }

    public VendorBusinessInfo updateBusinessInfo(Long id, VendorBusinessInfo businessInfo)
    {
        VendorBusinessInfo existing = getBusinessInfoById(id);
        businessInfo.setId(existing.getId());
        businessInfo.setVendorProfile(existing.getVendorProfile());
        return vendorBusinessInfoRepository.save(businessInfo);
    }

    public VendorBusinessInfo patchBusinessInfo(Long id, VendorBusinessInfo businessInfo)
    {
        VendorBusinessInfo existing = getBusinessInfoById(id);
        if (businessInfo.getName() != null) existing.setName(businessInfo.getName());
        if (businessInfo.getContactDetail() != null) existing.setContactDetail(businessInfo.getContactDetail());
        if (businessInfo.getAddress() != null) existing.setAddress(businessInfo.getAddress());
        if (businessInfo.getGstNumber() != null) existing.setGstNumber(businessInfo.getGstNumber());
        if (businessInfo.getFssaiNumber() != null) existing.setFssaiNumber(businessInfo.getFssaiNumber());
        if (businessInfo.getYearsInBusiness() != null) existing.setYearsInBusiness(businessInfo.getYearsInBusiness());
        return vendorBusinessInfoRepository.save(existing);
    }
}
