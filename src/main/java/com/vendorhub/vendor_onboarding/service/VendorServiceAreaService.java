package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import com.vendorhub.vendor_onboarding.exception.VendorNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorServiceRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class VendorServiceAreaService {

    private VendorServiceRepository vendorServiceRepository;

    VendorServiceAreaService(VendorServiceRepository vendorServiceRepository)
    {
        this.vendorServiceRepository=vendorServiceRepository;
    }


    public VendorServiceArea createVendorServiceArea(  VendorServiceArea vendorServiceArea)
    {
        return vendorServiceRepository.save(vendorServiceArea);
    }


    public List<VendorServiceArea> getAllVendorServiceArea()
    {

        return vendorServiceRepository.findAll();
    }


    public VendorServiceArea getVendorServiceAreaById( Long id)
    {

        return vendorServiceRepository.findById(id).orElseThrow(()->new VendorNotFoundException(id));
    }


    public void deleteVendorServiceAreaById( Long id)
    {
        if(!vendorServiceRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        vendorServiceRepository.deleteById(id);
    }


    public VendorServiceArea updateVendorServiceArea( Long id, VendorServiceArea vendorServiceArea)
    {
        if(!vendorServiceRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        vendorServiceArea.setId(id);
        return vendorServiceRepository.save(vendorServiceArea);
    }


    public VendorServiceArea updatedVendorServiceAreas( Long id ,  VendorServiceArea vendorServiceArea)
    {
        return vendorServiceRepository.findById(id).map(existing->{

            existing.setServiceCity(vendorServiceArea.getServiceCity());
            existing.setServicePIN(vendorServiceArea.getServicePIN());
            existing.setServiceCountry(vendorServiceArea.getServiceCountry());
            existing.setMaxPeople(vendorServiceArea.getMaxPeople());
            existing.setMaximumOrderSize(vendorServiceArea.getMaximumOrderSize());
            existing.setMinimumOrderSize(vendorServiceArea.getMinimumOrderSize());
            existing.setMinOrderValue(vendorServiceArea.getMinOrderValue());

            return vendorServiceRepository.save(existing);

        }).orElseThrow(()->new VendorNotFoundException(id));
    }
}
