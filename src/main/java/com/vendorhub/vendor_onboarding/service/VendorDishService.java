package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.dto.VendorDishRequest;
import com.vendorhub.vendor_onboarding.dto.VendorDishResponse;
import com.vendorhub.vendor_onboarding.entity.VendorDish;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorDishRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VendorDishService {

    private final VendorDishRepository vendorDishRepository;
    private final DishService dishService;
    private final CurrentVendor currentVendor;

    VendorDishService(VendorDishRepository vendorDishRepository, DishService dishService, CurrentVendor currentVendor)
    {
        this.vendorDishRepository = vendorDishRepository;
        this.dishService = dishService;
        this.currentVendor = currentVendor;
    }

    public VendorDishResponse createVendorDish(VendorDishRequest request)
    {
        VendorDish vendorDish = new VendorDish();
        vendorDish.setVendor(currentVendor.vendor());
        vendorDish.setDish(dishService.findDish(request.dishId()));
        vendorDish.setPrice(request.price());
        vendorDish.setAvailable(request.available());
        return VendorDishResponse.from(vendorDishRepository.save(vendorDish));
    }

    public PageResponse<VendorDishResponse> getMyVendorDishes(Pageable pageable)
    {
        return PageResponse.from(vendorDishRepository.findByVendorId(currentVendor.id(), pageable), VendorDishResponse::from);
    }

    public VendorDishResponse getVendorDishById(Long id)
    {
        return VendorDishResponse.from(findOwned(id));
    }

    public void deleteVendorDish(Long id)
    {
        vendorDishRepository.delete(findOwned(id));
    }

    public VendorDishResponse updateVendorDish(Long id, VendorDishRequest request)
    {
        VendorDish existing = findOwned(id);
        existing.setDish(dishService.findDish(request.dishId()));
        existing.setPrice(request.price());
        existing.setAvailable(request.available());
        return VendorDishResponse.from(vendorDishRepository.save(existing));
    }

    public VendorDishResponse patchVendorDish(Long id, VendorDishRequest request)
    {
        VendorDish existing = findOwned(id);
        if (request.dishId() != null) existing.setDish(dishService.findDish(request.dishId()));
        if (request.price() != null) existing.setPrice(request.price());
        if (request.available() != null) existing.setAvailable(request.available());
        return VendorDishResponse.from(vendorDishRepository.save(existing));
    }

    private VendorDish findOwned(Long id)
    {
        return vendorDishRepository.findByIdAndVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor dish", id));
    }
}
