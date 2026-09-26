package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.MediaRequest;
import com.vendorhub.vendor_onboarding.dto.MediaResponse;
import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorMediaRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorMediaService {

    private final VendorMediaRepository vendorMediaRepository;
    private final CurrentVendor currentVendor;

    VendorMediaService(VendorMediaRepository vendorMediaRepository, CurrentVendor currentVendor)
    {
        this.vendorMediaRepository = vendorMediaRepository;
        this.currentVendor = currentVendor;
    }

    public MediaResponse createMedia(MediaRequest request)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorMediaRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Media already exists. Use PUT or PATCH to change it.");
        }
        VendorMedia media = new VendorMedia();
        request.applyTo(media);
        media.setVendorProfile(profile);
        return MediaResponse.from(vendorMediaRepository.save(media));
    }

    public List<MediaResponse> getMyMedia()
    {
        return vendorMediaRepository.findByVendorProfileVendorId(currentVendor.id()).stream()
                .map(MediaResponse::from)
                .toList();
    }

    public MediaResponse getMediaById(Long id)
    {
        return MediaResponse.from(findOwned(id));
    }

    public void deleteMedia(Long id)
    {
        vendorMediaRepository.delete(findOwned(id));
    }

    public MediaResponse updateMedia(Long id, MediaRequest request)
    {
        VendorMedia existing = findOwned(id);
        request.applyTo(existing);
        return MediaResponse.from(vendorMediaRepository.save(existing));
    }

    public MediaResponse patchMedia(Long id, MediaRequest request)
    {
        VendorMedia existing = findOwned(id);
        request.patch(existing);
        return MediaResponse.from(vendorMediaRepository.save(existing));
    }

    private VendorMedia findOwned(Long id)
    {
        return vendorMediaRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Media", id));
    }
}
