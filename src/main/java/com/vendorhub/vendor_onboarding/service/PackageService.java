package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.PackageDishRepository;
import com.vendorhub.vendor_onboarding.repository.PackageRepository;
import com.vendorhub.vendor_onboarding.security.CurrentVendor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public MenuPackage createPackage(MenuPackage menuPackage)
    {
        menuPackage.setId(null);
        menuPackage.setVendor(currentVendor.vendor());
        return packageRepository.save(menuPackage);
    }

    public List<MenuPackage> getMyPackages()
    {
        return packageRepository.findByVendorId(currentVendor.id());
    }

    public MenuPackage getPackageById(Long id)
    {
        return packageRepository.findByIdAndVendorId(id, currentVendor.id())
                .orElseThrow(() -> new ResourceNotFoundException("Package", id));
    }

    // Remove the dishes inside the package first, otherwise the package_dish foreign key blocks the delete
    @Transactional
    public void deletePackage(Long id)
    {
        MenuPackage menuPackage = getPackageById(id);
        packageDishRepository.deleteByMenuPackageId(menuPackage.getId());
        packageRepository.delete(menuPackage);
    }

    public MenuPackage updatePackage(Long id, MenuPackage menuPackage)
    {
        MenuPackage existing = getPackageById(id);
        menuPackage.setId(existing.getId());
        menuPackage.setVendor(existing.getVendor());
        return packageRepository.save(menuPackage);
    }

    public MenuPackage patchPackage(Long id, MenuPackage menuPackage)
    {
        MenuPackage existing = getPackageById(id);
        if (menuPackage.getName() != null) existing.setName(menuPackage.getName());
        if (menuPackage.getDescription() != null) existing.setDescription(menuPackage.getDescription());
        if (menuPackage.getPrice() != null) existing.setPrice(menuPackage.getPrice());
        if (menuPackage.getPriceUnit() != null) existing.setPriceUnit(menuPackage.getPriceUnit());
        if (menuPackage.getActive() != null) existing.setActive(menuPackage.getActive());
        return packageRepository.save(existing);
    }
}
