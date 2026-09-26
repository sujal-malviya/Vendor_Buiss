package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.RequiredItemRequest;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.dto.RequiredItemResponse;
import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.RequiredItemRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RequiredItemService {

    private final RequiredItemRepository requiredItemRepository;

    RequiredItemService(RequiredItemRepository requiredItemRepository)
    {
        this.requiredItemRepository = requiredItemRepository;
    }

    public RequiredItemResponse createRequiredItem(RequiredItemRequest request)
    {
        RequiredItem requiredItem = new RequiredItem();
        request.applyTo(requiredItem);
        return RequiredItemResponse.from(requiredItemRepository.save(requiredItem));
    }

    public PageResponse<RequiredItemResponse> getAllRequiredItems(Pageable pageable)
    {
        return PageResponse.from(requiredItemRepository.findAll(pageable), RequiredItemResponse::from);
    }

    public RequiredItemResponse getRequiredItemById(Long id)
    {
        return RequiredItemResponse.from(findRequiredItem(id));
    }

    public void deleteRequiredItem(Long id)
    {
        requiredItemRepository.delete(findRequiredItem(id));
    }

    public RequiredItemResponse updateRequiredItem(Long id, RequiredItemRequest request)
    {
        RequiredItem existing = findRequiredItem(id);
        request.applyTo(existing);
        return RequiredItemResponse.from(requiredItemRepository.save(existing));
    }

    public RequiredItemResponse patchRequiredItem(Long id, RequiredItemRequest request)
    {
        RequiredItem existing = findRequiredItem(id);
        request.patch(existing);
        return RequiredItemResponse.from(requiredItemRepository.save(existing));
    }

    private RequiredItem findRequiredItem(Long id)
    {
        return requiredItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Required item", id));
    }
}
