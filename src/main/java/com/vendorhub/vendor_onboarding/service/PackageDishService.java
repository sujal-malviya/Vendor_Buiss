package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.PackageDishRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageDishService {

    private final PackageDishRepository packageDishRepository;
    private final PackageService packageService;
    private final DishService dishService;
    private final CurrentVendor currentVendor;

    PackageDishService(PackageDishRepository packageDishRepository, PackageService packageService,
                       DishService dishService, CurrentVendor currentVendor)
    {
        this.packageDishRepository = packageDishRepository;
        this.packageService = packageService;
        this.dishService = dishService;
        this.currentVendor = currentVendor;
    }

    public PackageDish createPackageDish(PackageDish packageDish)
    {
        packageDish.setId(null);
        // Load the real rows: the package must belong to this vendor, and both must exist
        packageDish.setMenuPackage(packageService.getPackageById(packageDish.getMenuPackage().getId()));
        packageDish.setDish(dishService.getDishById(packageDish.getDish().getId()));
        return packageDishRepository.save(packageDish);
    }

    public List<PackageDish> getMyPackageDishes()
    {
        return packageDishRepository.findByMenuPackageVendorId(currentVendor.id());
    }

    public PackageDish getPackageDishById(Long id)
    {
        return packageDishRepository.findByIdAndMenuPackageVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Package dish", id));
    }

    public void deletePackageDish(Long id)
    {
        packageDishRepository.delete(getPackageDishById(id));
    }

    public PackageDish updatePackageDish(Long id, PackageDish packageDish)
    {
        PackageDish existing = getPackageDishById(id);
        existing.setMenuPackage(packageService.getPackageById(packageDish.getMenuPackage().getId()));
        existing.setDish(dishService.getDishById(packageDish.getDish().getId()));
        existing.setQuantity(packageDish.getQuantity());
        return packageDishRepository.save(existing);
    }

    public PackageDish patchPackageDish(Long id, PackageDish packageDish)
    {
        PackageDish existing = getPackageDishById(id);
        if (packageDish.getMenuPackage() != null)
        {
            existing.setMenuPackage(packageService.getPackageById(packageDish.getMenuPackage().getId()));
        }
        if (packageDish.getDish() != null) existing.setDish(dishService.getDishById(packageDish.getDish().getId()));
        if (packageDish.getQuantity() != null) existing.setQuantity(packageDish.getQuantity());
        return packageDishRepository.save(existing);
    }
}
