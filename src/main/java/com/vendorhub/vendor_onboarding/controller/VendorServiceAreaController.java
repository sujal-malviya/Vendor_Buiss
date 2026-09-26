package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.ServiceAreaRequest;
import com.vendorhub.vendor_onboarding.dto.ServiceAreaResponse;
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
    public ServiceAreaResponse createServiceArea(@Valid @RequestBody ServiceAreaRequest request)
    {
        return vendorServiceAreaService.createServiceArea(request);
    }

    @GetMapping
    public List<ServiceAreaResponse> getMyServiceAreas()
    {
        return vendorServiceAreaService.getMyServiceAreas();
    }

    @GetMapping("/{id}")
    public ServiceAreaResponse getServiceAreaById(@PathVariable Long id)
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
    public ServiceAreaResponse updateServiceArea(@PathVariable Long id, @Valid @RequestBody ServiceAreaRequest request)
    {
        return vendorServiceAreaService.updateServiceArea(id, request);
    }

    @PatchMapping("/{id}")
    public ServiceAreaResponse patchServiceArea(@PathVariable Long id, @RequestBody ServiceAreaRequest request)
    {
        return vendorServiceAreaService.patchServiceArea(id, request);
    }
}
