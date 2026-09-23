package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import com.vendorhub.vendor_onboarding.exception.VendorNotFoundException;
import com.vendorhub.vendor_onboarding.repository.RequiredItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class RequireditemService {

    private RequiredItemRepository requiredItemRepository;

    RequireditemService(RequiredItemRepository requiredItemRepository)
    {
        this.requiredItemRepository=requiredItemRepository;
    }


    public RequiredItem createRequirement( RequiredItem requiredItem)
    {
        return requiredItemRepository.save(requiredItem);
    }


    public List<RequiredItem> getAllRequirements()
    {
        return requiredItemRepository.findAll();
    }


    public RequiredItem getAllRequirementsById( Long id)
    {
        return  requiredItemRepository.findById(id).orElseThrow(()->new VendorNotFoundException(id));
    }


    public void deleteRequirement( Long id)
    {
        if(!requiredItemRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        requiredItemRepository.deleteById(id);
    }


    public RequiredItem updateRequirement( Long id ,  RequiredItem requiredItem)
    {
        if(!requiredItemRepository.existsById(id))
        {
            throw new VendorNotFoundException(id);
        }
        requiredItem.setId(id);
        return  requiredItemRepository.save(requiredItem);
    }


    public RequiredItem updateRequirements( Long id , RequiredItem requiredItem)
    {
        return requiredItemRepository.findById(id).map(existing->{

            existing.setUnit(requiredItem.getUnit());
            existing.setName(requiredItem.getName());
            existing.setDescription(requiredItem.getDescription());
            existing.setActive(requiredItem.getActive());

            return requiredItemRepository.save(existing);
        }).orElseThrow(()->new VendorNotFoundException(id));
    }
}
