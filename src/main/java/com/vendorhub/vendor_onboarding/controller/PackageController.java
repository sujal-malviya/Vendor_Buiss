package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import com.vendorhub.vendor_onboarding.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/packages")
public class PackageController {

    private final PackageService packageService;

    PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuPackage createPackage(@Valid @RequestBody MenuPackage menuPackage)
    {
        return packageService.createPackage(menuPackage);
    }

    @GetMapping
    public List<MenuPackage> getMyPackages()
    {
        return packageService.getMyPackages();
    }

    @GetMapping("/{id}")
    public MenuPackage getPackageById(@PathVariable Long id)
    {
        return packageService.getPackageById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePackage(@PathVariable Long id)
    {
        packageService.deletePackage(id);
    }

    @PutMapping("/{id}")
    public MenuPackage updatePackage(@PathVariable Long id, @Valid @RequestBody MenuPackage menuPackage)
    {
        return packageService.updatePackage(id, menuPackage);
    }

    @PatchMapping("/{id}")
    public MenuPackage patchPackage(@PathVariable Long id, @RequestBody MenuPackage menuPackage)
    {
        return packageService.patchPackage(id, menuPackage);
    }
}
