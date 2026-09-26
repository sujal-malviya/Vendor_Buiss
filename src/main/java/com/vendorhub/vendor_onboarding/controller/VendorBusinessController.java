package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.BusinessInfoRequest;
import com.vendorhub.vendor_onboarding.dto.BusinessInfoResponse;
import com.vendorhub.vendor_onboarding.service.VendorBusinessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/business-info")
public class VendorBusinessController {

    private final VendorBusinessService vendorBusinessService;

    VendorBusinessController(VendorBusinessService vendorBusinessService)
    {
        this.vendorBusinessService = vendorBusinessService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessInfoResponse createBusinessInfo(@Valid @RequestBody BusinessInfoRequest request)
    {
        return vendorBusinessService.createBusinessInfo(request);
    }

    @GetMapping
    public List<BusinessInfoResponse> getMyBusinessInfo()
    {
        return vendorBusinessService.getMyBusinessInfo();
    }

    @GetMapping("/{id}")
    public BusinessInfoResponse getBusinessInfoById(@PathVariable Long id)
    {
        return vendorBusinessService.getBusinessInfoById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBusinessInfo(@PathVariable Long id)
    {
        vendorBusinessService.deleteBusinessInfo(id);
    }

    @PutMapping("/{id}")
    public BusinessInfoResponse updateBusinessInfo(@PathVariable Long id, @Valid @RequestBody BusinessInfoRequest request)
    {
        return vendorBusinessService.updateBusinessInfo(id, request);
    }

    @PatchMapping("/{id}")
    public BusinessInfoResponse patchBusinessInfo(@PathVariable Long id, @RequestBody BusinessInfoRequest request)
    {
        return vendorBusinessService.patchBusinessInfo(id, request);
    }
}
