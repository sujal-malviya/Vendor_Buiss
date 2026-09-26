package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.MenuPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<MenuPackage,Long> {

    Page<MenuPackage> findByVendorId(Long vendorId, Pageable pageable);

    Optional<MenuPackage> findByIdAndVendorId(Long id, Long vendorId);
}
