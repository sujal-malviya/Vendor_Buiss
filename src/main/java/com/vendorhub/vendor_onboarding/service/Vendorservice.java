package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.Vendor;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.repository.VendorProfileRepository;
import com.vendorhub.vendor_onboarding.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Vendorservice {

    private VendorRepository vendorRepository;
    private VendorProfileRepository vendorProfileRepository;
    Vendorservice(VendorRepository vendorRepository,VendorProfileRepository vendorProfileRepository)
    {
        this.vendorRepository = vendorRepository;
        this.vendorProfileRepository=vendorProfileRepository;
    }

    public List<VendorProfile> getAllVendor()
    {
        return vendorProfileRepository.findAll();
    }

    public VendorProfile createVendor(VendorProfile vendorprofile) {
        if (vendorprofile.getBuissnessInfo() != null) {
            vendorprofile.getBuissnessInfo().setVendorProfile(vendorprofile);
        }
        return vendorProfileRepository.save(vendorprofile);
    }

    public VendorProfile getVendorProfileById(Long id)
    {
        return vendorProfileRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found : "+id));
    }

    public void deleteVendorProfileById(Long id )
    {
        if(!vendorProfileRepository.existsById(id))
        {
            throw new RuntimeException("id not found : "+id);
        }
        vendorProfileRepository.deleteById(id);
    }

    public VendorProfile updateVendorProfile(Long id,VendorProfile vendorProfile)
    {
        if(!vendorProfileRepository.existsById(id))
        {
            throw new RuntimeException("id not found: "+id);
        }
        vendorProfile.setId(id);
        return vendorProfileRepository.save(vendorProfile);

    }


    public VendorProfile updatedVendorProfile (Long id , VendorProfile vendorProfile)
    {
        return vendorProfileRepository.findById(id).map(
                existingProfile ->{
                    existingProfile.setName(vendorProfile.getName());
                    existingProfile.setAddress(vendorProfile.getAddress());

                    return vendorProfileRepository.save(existingProfile);
                }
        ).orElseThrow(()-> new RuntimeException("id not found : "+id));


    }



}

