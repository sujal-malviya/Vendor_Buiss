package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.VendorProfileRequest;
import com.vendorhub.vendor_onboarding.dto.VendorProfileResponse;
import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.OrderPoliciesRepository;
import com.vendorhub.vendor_onboarding.repository.VendorBankRepository;
import com.vendorhub.vendor_onboarding.repository.VendorMediaRepository;
import com.vendorhub.vendor_onboarding.repository.VendorPaymentRepository;
import com.vendorhub.vendor_onboarding.repository.VendorProfileRepository;
import com.vendorhub.vendor_onboarding.repository.VendorServiceAreaRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorProfileService {

    private final VendorProfileRepository vendorProfileRepository;
    private final VendorBankRepository vendorBankRepository;
    private final VendorMediaRepository vendorMediaRepository;
    private final VendorPaymentRepository vendorPaymentRepository;
    private final VendorServiceAreaRepository vendorServiceAreaRepository;
    private final OrderPoliciesRepository orderPoliciesRepository;
    private final CurrentVendor currentVendor;

    VendorProfileService(VendorProfileRepository vendorProfileRepository,
                         VendorBankRepository vendorBankRepository,
                         VendorMediaRepository vendorMediaRepository,
                         VendorPaymentRepository vendorPaymentRepository,
                         VendorServiceAreaRepository vendorServiceAreaRepository,
                         OrderPoliciesRepository orderPoliciesRepository,
                         CurrentVendor currentVendor)
    {
        this.vendorProfileRepository = vendorProfileRepository;
        this.vendorBankRepository = vendorBankRepository;
        this.vendorMediaRepository = vendorMediaRepository;
        this.vendorPaymentRepository = vendorPaymentRepository;
        this.vendorServiceAreaRepository = vendorServiceAreaRepository;
        this.orderPoliciesRepository = orderPoliciesRepository;
        this.currentVendor = currentVendor;
    }

    public VendorProfileResponse createProfile(VendorProfileRequest request)
    {
        if (vendorProfileRepository.existsByVendorId(currentVendor.id()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have a vendor profile. Use PUT or PATCH to change it.");
        }
        VendorProfile profile = new VendorProfile();
        request.applyTo(profile);
        profile.setVendor(currentVendor.vendor());
        if (request.businessInfo() != null)
        {
            VendorBusinessInfo businessInfo = new VendorBusinessInfo();
            request.businessInfo().applyTo(businessInfo);
            businessInfo.setVendorProfile(profile);
            profile.setBusinessInfo(businessInfo);
        }
        return VendorProfileResponse.from(vendorProfileRepository.save(profile));
    }

    // A vendor has at most one profile, so this list has 0 or 1 items
    public List<VendorProfileResponse> getMyProfiles()
    {
        return vendorProfileRepository.findByVendorId(currentVendor.id()).stream()
                .map(VendorProfileResponse::from)
                .toList();
    }

    public VendorProfileResponse getProfileById(Long id)
    {
        return VendorProfileResponse.from(findOwned(id));
    }

    // Deletes the profile and every section that belongs to it, so no foreign key blocks the delete
    @Transactional
    public void deleteProfile(Long id)
    {
        VendorProfile profile = findOwned(id);
        vendorBankRepository.deleteByVendorProfileId(profile.getId());
        vendorMediaRepository.deleteByVendorProfileId(profile.getId());
        vendorPaymentRepository.deleteByVendorProfileId(profile.getId());
        vendorServiceAreaRepository.deleteByVendorProfileId(profile.getId());
        orderPoliciesRepository.deleteByVendorProfileId(profile.getId());
        vendorProfileRepository.delete(profile);   // business info is removed by CascadeType.ALL
    }

    // Business info has its own endpoint (/api/vendor/business-info), so PUT only replaces the profile's own fields
    public VendorProfileResponse updateProfile(Long id, VendorProfileRequest request)
    {
        VendorProfile existing = findOwned(id);
        request.applyTo(existing);
        return VendorProfileResponse.from(vendorProfileRepository.save(existing));
    }

    public VendorProfileResponse patchProfile(Long id, VendorProfileRequest request)
    {
        VendorProfile existing = findOwned(id);
        request.patch(existing);
        return VendorProfileResponse.from(vendorProfileRepository.save(existing));
    }

    private VendorProfile findOwned(Long id)
    {
        return vendorProfileRepository.findByIdAndVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor profile", id));
    }
}
