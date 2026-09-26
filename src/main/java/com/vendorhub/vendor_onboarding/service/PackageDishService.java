package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.PackageDishRequest;
import com.vendorhub.vendor_onboarding.dto.PackageDishResponse;
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

    public PackageDishResponse createPackageDish(PackageDishRequest request)
    {
        PackageDish packageDish = new PackageDish();
        // Load the real rows from the ids: the package must belong to this vendor, and both must exist
        packageDish.setMenuPackage(packageService.findOwnedPackage(request.packageId()));
        packageDish.setDish(dishService.findDish(request.dishId()));
        packageDish.setQuantity(request.quantity());
        return PackageDishResponse.from(packageDishRepository.save(packageDish));
    }

    public List<PackageDishResponse> getMyPackageDishes()
    {
        return packageDishRepository.findByMenuPackageVendorId(currentVendor.id()).stream()
                .map(PackageDishResponse::from)
                .toList();
    }

    public PackageDishResponse getPackageDishById(Long id)
    {
        return PackageDishResponse.from(findOwned(id));
    }

    public void deletePackageDish(Long id)
    {
        packageDishRepository.delete(findOwned(id));
    }

    public PackageDishResponse updatePackageDish(Long id, PackageDishRequest request)
    {
        PackageDish existing = findOwned(id);
        existing.setMenuPackage(packageService.findOwnedPackage(request.packageId()));
        existing.setDish(dishService.findDish(request.dishId()));
        existing.setQuantity(request.quantity());
        return PackageDishResponse.from(packageDishRepository.save(existing));
    }

    public PackageDishResponse patchPackageDish(Long id, PackageDishRequest request)
    {
        PackageDish existing = findOwned(id);
        if (request.packageId() != null) existing.setMenuPackage(packageService.findOwnedPackage(request.packageId()));
        if (request.dishId() != null) existing.setDish(dishService.findDish(request.dishId()));
        if (request.quantity() != null) existing.setQuantity(request.quantity());
        return PackageDishResponse.from(packageDishRepository.save(existing));
    }

    private PackageDish findOwned(Long id)
    {
        return packageDishRepository.findByIdAndMenuPackageVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Package dish", id));
    }
}
