package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.RequiredItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequiredItemService {

    private final RequiredItemRepository requiredItemRepository;

    RequiredItemService(RequiredItemRepository requiredItemRepository)
    {
        this.requiredItemRepository = requiredItemRepository;
    }

    public RequiredItem createRequiredItem(RequiredItem requiredItem)
    {
        requiredItem.setId(null);
        return requiredItemRepository.save(requiredItem);
    }

    public List<RequiredItem> getAllRequiredItems()
    {
        return requiredItemRepository.findAll();
    }

    public RequiredItem getRequiredItemById(Long id)
    {
        return requiredItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Required item", id));
    }

    public void deleteRequiredItem(Long id)
    {
        requiredItemRepository.delete(getRequiredItemById(id));
    }

    public RequiredItem updateRequiredItem(Long id, RequiredItem requiredItem)
    {
        getRequiredItemById(id);
        requiredItem.setId(id);
        return requiredItemRepository.save(requiredItem);
    }

    public RequiredItem patchRequiredItem(Long id, RequiredItem requiredItem)
    {
        RequiredItem existing = getRequiredItemById(id);
        if (requiredItem.getName() != null) existing.setName(requiredItem.getName());
        if (requiredItem.getDescription() != null) existing.setDescription(requiredItem.getDescription());
        if (requiredItem.getUnit() != null) existing.setUnit(requiredItem.getUnit());
        if (requiredItem.getActive() != null) existing.setActive(requiredItem.getActive());
        return requiredItemRepository.save(existing);
    }
}
