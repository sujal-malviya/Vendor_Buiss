package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorBuissnessInfo;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.repository.VendorBuissnessInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class VendorBuissnessService {

    private VendorBuissnessInfoRepository vendorBuissnessInfoRepository;

    VendorBuissnessService(VendorBuissnessInfoRepository vendorBuissnessInfoRepository)
    {
        this.vendorBuissnessInfoRepository=vendorBuissnessInfoRepository;
    }

    public VendorBuissnessInfo createVendorBuissnessProfile(VendorBuissnessInfo vendorBuissnessInfo)
    {
        return vendorBuissnessInfoRepository.save(vendorBuissnessInfo);
    }

    public List<VendorBuissnessInfo> getAllVendorBuissnessProfile()
    {
        return vendorBuissnessInfoRepository.findAll();
    }

    public VendorBuissnessInfo getVendorBuissnessProfileById(Long id)
    {
        return vendorBuissnessInfoRepository.findById(id).orElseThrow(()->new RuntimeException("Buissness ID not found : "+id));
    }

    public void deleteVendorBuissnessProfile( Long id)
    {
        if(!vendorBuissnessInfoRepository.existsById(id))
        {
            throw new RuntimeException("Buissness Id not found : "+id);
        }
        vendorBuissnessInfoRepository.deleteById(id);

    }


    public VendorBuissnessInfo updateBuissnessProfile(Long id,VendorBuissnessInfo vendorBuissnessInfo)
    {
        if(!vendorBuissnessInfoRepository.existsById(id))
        {
            throw new RuntimeException("Buissness id not founf : "+id);
        }
        vendorBuissnessInfo.setId(id);
        return vendorBuissnessInfoRepository.save(vendorBuissnessInfo);
    }

    public VendorBuissnessInfo updatedVendorProfile (Long id , VendorBuissnessInfo vendorBuissnessInfo)
    {
        return vendorBuissnessInfoRepository.findById(id).map(
                existingProfile ->{
                    existingProfile.setName(vendorBuissnessInfo.getName());
                    existingProfile.setAddress(vendorBuissnessInfo.getAddress());

                    return vendorBuissnessInfoRepository.save(existingProfile);
                }
        ).orElseThrow(()-> new RuntimeException("id not found : "+id));


    }


}
