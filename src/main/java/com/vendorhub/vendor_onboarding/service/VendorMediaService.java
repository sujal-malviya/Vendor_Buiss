package com.vendorhub.vendor_onboarding.service;

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

    public VendorMedia createMedia(VendorMedia media)
    {
        VendorProfile profile = currentVendor.profile();
        if (vendorMediaRepository.existsByVendorProfileId(profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Media already exists. Use PUT or PATCH to change it.");
        }
        media.setId(null);
        media.setVendorProfile(profile);
        return vendorMediaRepository.save(media);
    }

    public List<VendorMedia> getMyMedia()
    {
        return vendorMediaRepository.findByVendorProfileVendorId(currentVendor.id());
    }

    public VendorMedia getMediaById(Long id)
    {
        return vendorMediaRepository.findByIdAndVendorProfileVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Media", id));
    }

    public void deleteMedia(Long id)
    {
        vendorMediaRepository.delete(getMediaById(id));
    }

    public VendorMedia updateMedia(Long id, VendorMedia media)
    {
        VendorMedia existing = getMediaById(id);
        media.setId(existing.getId());
        media.setVendorProfile(existing.getVendorProfile());
        return vendorMediaRepository.save(media);
    }

    public VendorMedia patchMedia(Long id, VendorMedia media)
    {
        VendorMedia existing = getMediaById(id);
        if (media.getImage() != null) existing.setImage(media.getImage());
        if (media.getVideo() != null) existing.setVideo(media.getVideo());
        return vendorMediaRepository.save(existing);
    }
}
