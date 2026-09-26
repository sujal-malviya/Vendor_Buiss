package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.ServiceAreaRequest;
import com.vendorhub.vendor_onboarding.dto.ServiceAreaResponse;
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

    public ServiceAreaResponse createServiceArea(ServiceAreaRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorServiceAreaRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Service area already exists. Use PUT or PATCH to change it.");
        }
        VendorServiceArea serviceArea = new VendorServiceArea();
        request.applyTo(serviceArea);
        serviceArea.setVendorProfile(profile);
        return ServiceAreaResponse.from(vendorServiceAreaRepository.save(serviceArea));
    }

    public List<ServiceAreaResponse> getMyServiceAreas()
    {
        return vendorServiceAreaRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(ServiceAreaResponse::from)
                .toList();
    }

    public ServiceAreaResponse getServiceAreaById(Long id)
    {
        return ServiceAreaResponse.from(findOwned(id));
    }

    public void deleteServiceArea(Long id)
    {
        vendorServiceAreaRepository.delete(findOwned(id));
    }

    public ServiceAreaResponse updateServiceArea(Long id, ServiceAreaRequest request)
    {
        VendorServiceArea existing = findOwned(id);
        request.applyTo(existing);
        return ServiceAreaResponse.from(vendorServiceAreaRepository.save(existing));
    }

    public ServiceAreaResponse patchServiceArea(Long id, ServiceAreaRequest request)
    {
        VendorServiceArea existing = findOwned(id);
        request.patch(existing);
        return ServiceAreaResponse.from(vendorServiceAreaRepository.save(existing));
    }

    private VendorServiceArea findOwned(Long id)
    {
        return vendorServiceAreaRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Service area", id));
    }
}
