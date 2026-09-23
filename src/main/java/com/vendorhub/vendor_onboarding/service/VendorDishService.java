package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import com.vendorhub.vendor_onboarding.repository.VendorDishRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class VendorDishService {

    private VendorDishRepository vendorDishRepository;

    VendorDishService(VendorDishRepository vendorDishRepository)
    {
        this.vendorDishRepository = vendorDishRepository;
    }


    public VendorDish createVendorDish( VendorDish vendorDish)
    {
        return vendorDishRepository.save(vendorDish);
    }


    public List<VendorDish> getAllVendorDish()
    {
        return vendorDishRepository.findAll();
    }


    public VendorDish getAllVendorDishById(Long id)
    {
        return vendorDishRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found : "+id));
    }


    public void deleteVendorDish( Long id)
    {
        if(!vendorDishRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        vendorDishRepository.deleteById(id);
    }


    public VendorDish updateVendorDish( Long id, VendorDish vendorDish)
    {
        if(!vendorDishRepository.existsById(id))
        {
            throw new RuntimeException("Id not found : "+id);
        }
        vendorDish.setId(id);
        return vendorDishRepository.save(vendorDish);
    }


    public VendorDish updateVendorDishes(Long id,VendorDish vendorDish)
    {
        return vendorDishRepository.findById(id).map(existing->{
            existing.setId(vendorDish.getId());
            existing.setDish(vendorDish.getDish());
            existing.setVendor(vendorDish.getVendor());
            existing.setPrice(vendorDish.getPrice());
            existing.setAvailable(vendorDish.getAvailable());

            return vendorDishRepository.save(existing);
        }).orElseThrow(()->new RuntimeException("Id not found : "+id));
    }
}
