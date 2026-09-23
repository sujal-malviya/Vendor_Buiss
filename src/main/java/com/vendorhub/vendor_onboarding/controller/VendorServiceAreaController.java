package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import com.vendorhub.vendor_onboarding.service.VendorServiceAreaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/service-area")
class VendorServiceAreaController {

    private VendorServiceAreaService vendorServiceAreaService;

    VendorServiceAreaController(VendorServiceAreaService vendorServiceAreaService)
    {
        this.vendorServiceAreaService = vendorServiceAreaService;
    }

    @PostMapping
    public VendorServiceArea createVendorServiceArea(@Valid @RequestBody VendorServiceArea vendorServiceArea)
    {
        return vendorServiceAreaService.createVendorServiceArea(vendorServiceArea);
    }

    @GetMapping
    public List<VendorServiceArea> getAllVendorServiceArea()
    {

        return vendorServiceAreaService.getAllVendorServiceArea();
    }

    @GetMapping("/{id}")
    public VendorServiceArea getVendorServiceAreaById(@PathVariable Long id)
    {

        return vendorServiceAreaService.getVendorServiceAreaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorServiceAreaById(@PathVariable Long id)
    {

        vendorServiceAreaService.deleteVendorServiceAreaById(id);
    }

    @PutMapping("/{id}")
    public VendorServiceArea updateVendorServiceArea(@PathVariable Long id,@RequestBody VendorServiceArea vendorServiceArea)
    {
        return vendorServiceAreaService.updateVendorServiceArea(id,vendorServiceArea);
    }

    @PatchMapping("/{id}")
    public VendorServiceArea updatedVendorServiceAreas(@PathVariable Long id , @RequestBody VendorServiceArea vendorServiceArea)
    {
        return vendorServiceAreaService.updatedVendorServiceAreas(id,vendorServiceArea);
    }
}
