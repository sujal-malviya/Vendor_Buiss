package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.PackageDishRequest;
import com.vendorhub.vendor_onboarding.dto.PackageDishResponse;
import com.vendorhub.vendor_onboarding.service.PackageDishService;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/package-dishes")
public class PackageDishController {

    private final PackageDishService packageDishService;

    PackageDishController(PackageDishService packageDishService)
    {
        this.packageDishService = packageDishService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PackageDishResponse createPackageDish(@Valid @RequestBody PackageDishRequest request)
    {
        return packageDishService.createPackageDish(request);
    }

    @GetMapping
    public PageResponse<PackageDishResponse> getMyPackageDishes(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        return packageDishService.getMyPackageDishes(pageable);
    }

    @GetMapping("/{id}")
    public PackageDishResponse getPackageDishById(@PathVariable Long id)
    {
        return packageDishService.getPackageDishById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePackageDish(@PathVariable Long id)
    {
        packageDishService.deletePackageDish(id);
    }

    @PutMapping("/{id}")
    public PackageDishResponse updatePackageDish(@PathVariable Long id, @Valid @RequestBody PackageDishRequest request)
    {
        return packageDishService.updatePackageDish(id, request);
    }

    @PatchMapping("/{id}")
    public PackageDishResponse patchPackageDish(@PathVariable Long id, @RequestBody PackageDishRequest request)
    {
        return packageDishService.patchPackageDish(id, request);
    }
}
