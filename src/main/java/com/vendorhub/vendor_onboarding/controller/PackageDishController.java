package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import com.vendorhub.vendor_onboarding.service.PackageDishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PackageDish createPackageDish(@Valid @RequestBody PackageDish packageDish)
    {
        return packageDishService.createPackageDish(packageDish);
    }

    @GetMapping
    public List<PackageDish> getMyPackageDishes()
    {
        return packageDishService.getMyPackageDishes();
    }

    @GetMapping("/{id}")
    public PackageDish getPackageDishById(@PathVariable Long id)
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
    public PackageDish updatePackageDish(@PathVariable Long id, @Valid @RequestBody PackageDish packageDish)
    {
        return packageDishService.updatePackageDish(id, packageDish);
    }

    @PatchMapping("/{id}")
    public PackageDish patchPackageDish(@PathVariable Long id, @RequestBody PackageDish packageDish)
    {
        return packageDishService.patchPackageDish(id, packageDish);
    }
}
