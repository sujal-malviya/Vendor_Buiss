package com.vendorhub.vendor_onboarding.controller;


import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.entity.Package;
import com.vendorhub.vendor_onboarding.service.PackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private PackageService packageService;

    PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }

    @PostMapping
    public Package createPackage(@RequestBody Package packagee)
    {
        return packageService.createPackage(packagee);
    }

    @GetMapping
    public List<Package> getAllPackage()
    {
        return packageService.getAllPackage();
    }

    @GetMapping("/{id}")
    public Package getAllPackageById(@PathVariable Long id)
    {
        return packageService.getAllPackageById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable Long id)
    {
        packageService.deletePackage(id);
    }

    @PutMapping("/{id}")
    public Package updatePackage(@PathVariable Long id,@RequestBody Package packagee)
    {
        return packageService.updatePackage(id,packagee);
    }

    @PatchMapping("/{id}")
    public Package updatePackages(@PathVariable Long id,@RequestBody Package packagee)
    {
        return packageService.updatePackages(id,packagee);
    }
}
