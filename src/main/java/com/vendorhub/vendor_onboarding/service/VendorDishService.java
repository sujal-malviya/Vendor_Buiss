package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.VendorDishRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public VendorDish createVendorDish(VendorDish vendorDish)
    {
        vendorDish.setId(null);
        vendorDish.setVendor(currentVendor.vendor());
        vendorDish.setDish(dishService.getDishById(vendorDish.getDish().getId()));
        return vendorDishRepository.save(vendorDish);
    }

    public List<VendorDish> getMyVendorDishes()
    {
        return vendorDishRepository.findByVendorId(currentVendor.id());
    }

    public VendorDish getVendorDishById(Long id)
    {
        return vendorDishRepository.findByIdAndVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor dish", id));
    }

    public void deleteVendorDish(Long id)
    {
        vendorDishRepository.delete(getVendorDishById(id));
    }

    public VendorDish updateVendorDish(Long id, VendorDish vendorDish)
    {
        VendorDish existing = getVendorDishById(id);
        existing.setDish(dishService.getDishById(vendorDish.getDish().getId()));
        existing.setPrice(vendorDish.getPrice());
        existing.setAvailable(vendorDish.getAvailable());
        return vendorDishRepository.save(existing);
    }

    public VendorDish patchVendorDish(Long id, VendorDish vendorDish)
    {
        VendorDish existing = getVendorDishById(id);
        if (vendorDish.getDish() != null) existing.setDish(dishService.getDishById(vendorDish.getDish().getId()));
        if (vendorDish.getPrice() != null) existing.setPrice(vendorDish.getPrice());
        if (vendorDish.getAvailable() != null) existing.setAvailable(vendorDish.getAvailable());
        return vendorDishRepository.save(existing);
    }
}
