package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
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
    public VendorBusinessInfo createBusinessInfo(@Valid @RequestBody VendorBusinessInfo businessInfo)
    {
        return vendorBusinessService.createBusinessInfo(businessInfo);
    }

    @GetMapping
    public List<VendorBusinessInfo> getMyBusinessInfo()
    {
        return vendorBusinessService.getMyBusinessInfo();
    }

    @GetMapping("/{id}")
    public VendorBusinessInfo getBusinessInfoById(@PathVariable Long id)
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
    public VendorBusinessInfo updateBusinessInfo(@PathVariable Long id, @Valid @RequestBody VendorBusinessInfo businessInfo)
    {
        return vendorBusinessService.updateBusinessInfo(id, businessInfo);
    }

    @PatchMapping("/{id}")
    public VendorBusinessInfo patchBusinessInfo(@PathVariable Long id, @RequestBody VendorBusinessInfo businessInfo)
    {
        return vendorBusinessService.patchBusinessInfo(id, businessInfo);
    }
}
