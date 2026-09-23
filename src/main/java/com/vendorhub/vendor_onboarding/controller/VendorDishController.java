package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import com.vendorhub.vendor_onboarding.service.VendorDishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// A vendor's own menu: which catalog dishes they offer, at what price
@RestController
@RequestMapping("/api/vendor/dishes")
public class VendorDishController {

    private final VendorDishService vendorDishService;

    VendorDishController(VendorDishService vendorDishService)
    {
        this.vendorDishService = vendorDishService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDish createVendorDish(@Valid @RequestBody VendorDish vendorDish)
    {
        return vendorDishService.createVendorDish(vendorDish);
    }

    @GetMapping
    public List<VendorDish> getMyVendorDishes()
    {
        return vendorDishService.getMyVendorDishes();
    }

    @GetMapping("/{id}")
    public VendorDish getVendorDishById(@PathVariable Long id)
    {
        return vendorDishService.getVendorDishById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendorDish(@PathVariable Long id)
    {
        vendorDishService.deleteVendorDish(id);
    }

    @PutMapping("/{id}")
    public VendorDish updateVendorDish(@PathVariable Long id, @Valid @RequestBody VendorDish vendorDish)
    {
        return vendorDishService.updateVendorDish(id, vendorDish);
    }

    @PatchMapping("/{id}")
    public VendorDish patchVendorDish(@PathVariable Long id, @RequestBody VendorDish vendorDish)
    {
        return vendorDishService.patchVendorDish(id, vendorDish);
    }
}
