package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import com.vendorhub.vendor_onboarding.service.VendorServiceAreaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/service-area")
public class VendorServiceAreaController {

    private final VendorServiceAreaService vendorServiceAreaService;

    VendorServiceAreaController(VendorServiceAreaService vendorServiceAreaService)
    {
        this.vendorServiceAreaService = vendorServiceAreaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorServiceArea createServiceArea(@Valid @RequestBody VendorServiceArea serviceArea)
    {
        return vendorServiceAreaService.createServiceArea(serviceArea);
    }

    @GetMapping
    public List<VendorServiceArea> getMyServiceAreas()
    {
        return vendorServiceAreaService.getMyServiceAreas();
    }

    @GetMapping("/{id}")
    public VendorServiceArea getServiceAreaById(@PathVariable Long id)
    {
        return vendorServiceAreaService.getServiceAreaById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceArea(@PathVariable Long id)
    {
        vendorServiceAreaService.deleteServiceArea(id);
    }

    @PutMapping("/{id}")
    public VendorServiceArea updateServiceArea(@PathVariable Long id, @Valid @RequestBody VendorServiceArea serviceArea)
    {
        return vendorServiceAreaService.updateServiceArea(id, serviceArea);
    }

    @PatchMapping("/{id}")
    public VendorServiceArea patchServiceArea(@PathVariable Long id, @RequestBody VendorServiceArea serviceArea)
    {
        return vendorServiceAreaService.patchServiceArea(id, serviceArea);
    }
}
