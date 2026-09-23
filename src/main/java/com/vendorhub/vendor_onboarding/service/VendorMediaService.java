package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.exception.VendorNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorMediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorMediaService {

    private VendorMediaRepository vendorMediaRepository;

    VendorMediaService(VendorMediaRepository vendorMediaRepository)
    {
        this.vendorMediaRepository=vendorMediaRepository;
    }

    public List<VendorMedia> getAllVendorMedia()
    {

        return vendorMediaRepository.findAll();
    }

    public VendorMedia createVendorMedia(VendorMedia vendorMedia) {

        return vendorMediaRepository.save(vendorMedia);
    }

    public VendorMedia getVendorProfileMediaById(Long id)
    {
        return vendorMediaRepository.findById(id).orElseThrow(()-> new VendorNotFoundException(id));
    }

    public void deleteVendorMedia(Long id )
    {
        if(!vendorMediaRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        vendorMediaRepository.deleteById(id);
    }

    public VendorMedia updateVendorMedia(Long id,VendorMedia vendorMedia)
    {
        if(!vendorMediaRepository.existsById(id))
        {
            throw new RuntimeException("id not found: "+id);
        }
        vendorMedia.setId(id);
        return vendorMediaRepository.save(vendorMedia);

    }


    public VendorMedia updatedVendorMedias (Long id , VendorMedia vendorMedia)
    {
        return vendorMediaRepository.findById(id).map(
                existingProfile ->{
                    existingProfile.setImage(vendorMedia.getImage());
                    existingProfile.setVideo(vendorMedia.getVideo());

                    return vendorMediaRepository.save(existingProfile);
                }
        ).orElseThrow(()-> new VendorNotFoundException(id));


    }
}
