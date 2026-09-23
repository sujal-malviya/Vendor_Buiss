package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<MenuPackage,Long> {

    List<MenuPackage> findByVendorId(Long vendorId);

    Optional<MenuPackage> findByIdAndVendorId(Long id, Long vendorId);
}
