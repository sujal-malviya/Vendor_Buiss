package com.vendorhub.vendor_onboarding.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vendorhub.vendor_onboarding.entity.VendorBuissnessInfo;
import com.vendorhub.vendor_onboarding.service.VendorBuissnessService;

@RestController
@RequestMapping("/api/vendor/buissness")
public class VendorBuissnessProfileController {

    private VendorBuissnessService vendorBuissnessService;
    VendorBuissnessProfileController(VendorBuissnessService vendorBuissnessService)
    {
        this.vendorBuissnessService = vendorBuissnessService;
    }
    @PostMapping
    public VendorBuissnessInfo createVendorBuissnessProfile(@RequestBody VendorBuissnessInfo vendorBuissnessInfo)
    {
        return vendorBuissnessService.createVendorBuissnessProfile(vendorBuissnessInfo);
    }

    @GetMapping
    public List<VendorBuissnessInfo> getAllVendorBuissnessProfile()
    {
        return vendorBuissnessService.getAllVendorBuissnessProfile();
    }

    @GetMapping("/profile/{id}")
    public VendorBuissnessInfo getVendorBuissnessProfileById(@PathVariable Long id)
    {
        return vendorBuissnessService.getVendorBuissnessProfileById(id);
    }

    @DeleteMapping("/profile/{id}")
    public void deleteVendorBuissnessProfile(@PathVariable Long id)
    {
        vendorBuissnessService.deleteVendorBuissnessProfile(id);
    }

    @PutMapping("/profile/{id}")
    public VendorBuissnessInfo updateBuissnessProfile(@PathVariable Long id,@RequestBody VendorBuissnessInfo vendorBuissnessInfo)
    {
        return vendorBuissnessService.updateBuissnessProfile(id,vendorBuissnessInfo);
    }



}
