package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.dto.PackageRequest;
import com.vendorhub.vendor_onboarding.dto.PackageResponse;
import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.PackageDishRepository;
import com.vendorhub.vendor_onboarding.repository.PackageRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageDishRepository packageDishRepository;
    private final CurrentVendor currentVendor;

    PackageService(PackageRepository packageRepository, PackageDishRepository packageDishRepository, CurrentVendor currentVendor)
    {
        this.packageRepository = packageRepository;
        this.packageDishRepository = packageDishRepository;
        this.currentVendor = currentVendor;
    }

    public PackageResponse createPackage(PackageRequest request)
    {
        MenuPackage menuPackage = new MenuPackage();
        request.applyTo(menuPackage);
        menuPackage.setVendor(currentVendor.vendor());
        return PackageResponse.from(packageRepository.save(menuPackage));
    }

    public PageResponse<PackageResponse> getMyPackages(Pageable pageable)
    {
        return PageResponse.from(packageRepository.findByVendorId(currentVendor.id(), pageable), PackageResponse::from);
    }

    public PackageResponse getPackageById(Long id)
    {
        return PackageResponse.from(findOwnedPackage(id));
    }

    // Remove the dishes inside the package first, otherwise the package_dish foreign key blocks the delete
    @Transactional
    public void deletePackage(Long id)
    {
        MenuPackage menuPackage = findOwnedPackage(id);
        packageDishRepository.deleteByMenuPackageId(menuPackage.getId());
        packageRepository.delete(menuPackage);
    }

    public PackageResponse updatePackage(Long id, PackageRequest request)
    {
        MenuPackage existing = findOwnedPackage(id);
        request.applyTo(existing);
        return PackageResponse.from(packageRepository.save(existing));
    }

    public PackageResponse patchPackage(Long id, PackageRequest request)
    {
        MenuPackage existing = findOwnedPackage(id);
        request.patch(existing);
        return PackageResponse.from(packageRepository.save(existing));
    }

    // Public because PackageDishService needs the entity to check the package belongs to this vendor
    public MenuPackage findOwnedPackage(Long id)
    {
        return packageRepository.findByIdAndVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Package", id));
    }
}
