package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import com.vendorhub.vendor_onboarding.service.PackageDishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/package-dieshes")
public class PackageDishController {

    private PackageDishService packageDishService;

    PackageDishController(PackageDishService packageDishService)
    {
        this.packageDishService=packageDishService;
    }

    @PostMapping
    public PackageDish createPackageDish(@RequestBody PackageDish packageDish)
    {
        return packageDishService.createPackageDish(packageDish);
    }

    @GetMapping
    public List<PackageDish> getAllPackageDish()
    {
        return packageDishService.getAllPackageDish();
    }

    @GetMapping("/{id}")
    public PackageDish getAllPackageDishById(@PathVariable Long id)
    {
        return packageDishService.getAllPackageDishById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePackageDish(@PathVariable Long id)
    {
        packageDishService.deletePackageDish(id);
    }

    @PutMapping("/{id}")
    public PackageDish updatePackageDish(@PathVariable Long id,@RequestBody PackageDish packageDish)
    {
        return packageDishService.updatePackageDish(id,packageDish);
    }

    @PatchMapping("/{id}")
    public PackageDish updatePackageDishes(@PathVariable Long id,@RequestBody PackageDish packageDish)
    {
        return packageDishService.updatePackageDishes(id,packageDish);
    }

}
