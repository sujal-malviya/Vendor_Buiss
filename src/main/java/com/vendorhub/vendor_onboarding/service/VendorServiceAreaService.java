package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorServiceAreaRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorServiceAreaService {

    private final VendorServiceAreaRepository vendorServiceAreaRepository;
    private final CurrentVendor currentVendor;

    VendorServiceAreaService(VendorServiceAreaRepository vendorServiceAreaRepository, CurrentVendor currentVendor)
    {
        this.vendorServiceAreaRepository = vendorServiceAreaRepository;
        this.currentVendor = currentVendor;
    }

    public VendorServiceArea createServiceArea(VendorServiceArea serviceArea)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorServiceAreaRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Service area already exists. Use PUT or PATCH to change it.");
        }
        serviceArea.setId(null);
        serviceArea.setVendorProfile(profile);
        return vendorServiceAreaRepository.save(serviceArea);
    }

    public List<VendorServiceArea> getMyServiceAreas()
    {
        return vendorServiceAreaRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    public VendorServiceArea getServiceAreaById(Long id)
    {
        return vendorServiceAreaRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Service area", id));
    }

    public void deleteServiceArea(Long id)
    {
        vendorServiceAreaRepository.delete(getServiceAreaById(id));
    }

    public VendorServiceArea updateServiceArea(Long id, VendorServiceArea serviceArea)
    {
        VendorServiceArea existing = getServiceAreaById(id);
        serviceArea.setId(existing.getId());
        serviceArea.setVendorProfile(existing.getVendorProfile());
        return vendorServiceAreaRepository.save(serviceArea);
    }

    public VendorServiceArea patchServiceArea(Long id, VendorServiceArea serviceArea)
    {
        VendorServiceArea existing = getServiceAreaById(id);
        if (serviceArea.getMaxPeople() != null) existing.setMaxPeople(serviceArea.getMaxPeople());
        if (serviceArea.getServicePin() != null) existing.setServicePin(serviceArea.getServicePin());
        if (serviceArea.getServiceCity() != null) existing.setServiceCity(serviceArea.getServiceCity());
        if (serviceArea.getServiceCountry() != null) existing.setServiceCountry(serviceArea.getServiceCountry());
        if (serviceArea.getOrderSize() != null) existing.setOrderSize(serviceArea.getOrderSize());
        if (serviceArea.getMinimumOrderSize() != null) existing.setMinimumOrderSize(serviceArea.getMinimumOrderSize());
        if (serviceArea.getMaximumOrderSize() != null) existing.setMaximumOrderSize(serviceArea.getMaximumOrderSize());
        if (serviceArea.getMinOrderValue() != null) existing.setMinOrderValue(serviceArea.getMinOrderValue());
        return vendorServiceAreaRepository.save(existing);
    }
}
