package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import com.vendorhub.vendor_onboarding.service.VendorDishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/vendor-dishes")
public class VendorDishController {

    private VendorDishService vendorDishService;

    VendorDishController(VendorDishService vendorDishService)
    {
        this.vendorDishService = vendorDishService;
    }

    @PostMapping
    public VendorDish createVendorDish(@RequestBody VendorDish vendorDish)
    {
        return vendorDishService.createVendorDish(vendorDish);
    }

    @GetMapping
    public List<VendorDish> getAllVendorDish()
    {
        return vendorDishService.getAllVendorDish();
    }

    @GetMapping("/{id}")
    public VendorDish getAllVendorDishById(@PathVariable Long id)
    {
        return vendorDishService.getAllVendorDishById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorDish(@PathVariable Long id)
    {
        vendorDishService.deleteVendorDish(id);
    }

    @PutMapping("/{id}")
    public VendorDish updateVendorDish(@PathVariable Long id,@RequestBody VendorDish vendorDish)
    {
        return vendorDishService.updateVendorDish(id,vendorDish);
    }

    @PatchMapping("/{id}")
    public VendorDish updateVendorDishes(@PathVariable Long id,@RequestBody VendorDish vendorDish)
    {
        return vendorDishService.updateVendorDishes(id,vendorDish);
    }
}
