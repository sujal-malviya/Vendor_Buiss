package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.VendorDishRequest;
import com.vendorhub.vendor_onboarding.dto.VendorDishResponse;
import com.vendorhub.vendor_onboarding.service.VendorDishService;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public VendorDishResponse createVendorDish(@Valid @RequestBody VendorDishRequest request)
    {
        return vendorDishService.createVendorDish(request);
    }

    @GetMapping
    public PageResponse<VendorDishResponse> getMyVendorDishes(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        return vendorDishService.getMyVendorDishes(pageable);
    }

    @GetMapping("/{id}")
    public VendorDishResponse getVendorDishById(@PathVariable Long id)
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
    public VendorDishResponse updateVendorDish(@PathVariable Long id, @Valid @RequestBody VendorDishRequest request)
    {
        return vendorDishService.updateVendorDish(id, request);
    }

    @PatchMapping("/{id}")
    public VendorDishResponse patchVendorDish(@PathVariable Long id, @RequestBody VendorDishRequest request)
    {
        return vendorDishService.patchVendorDish(id, request);
    }
}
