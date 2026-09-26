package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.PackageRequest;
import com.vendorhub.vendor_onboarding.dto.PackageResponse;
import com.vendorhub.vendor_onboarding.service.PackageService;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PackageResponse createPackage(@Valid @RequestBody PackageRequest request)
    {
        return packageService.createPackage(request);
    }

    @GetMapping
    public PageResponse<PackageResponse> getMyPackages(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        return packageService.getMyPackages(pageable);
    }

    @GetMapping("/{id}")
    public PackageResponse getPackageById(@PathVariable Long id)
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
    public PackageResponse updatePackage(@PathVariable Long id, @Valid @RequestBody PackageRequest request)
    {
        return packageService.updatePackage(id, request);
    }

    @PatchMapping("/{id}")
    public PackageResponse patchPackage(@PathVariable Long id, @RequestBody PackageRequest request)
    {
        return packageService.patchPackage(id, request);
    }
}
