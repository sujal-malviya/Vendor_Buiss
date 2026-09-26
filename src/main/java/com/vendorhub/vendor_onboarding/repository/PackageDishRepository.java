package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PackageDishRepository extends JpaRepository<PackageDish,Long> {

    Page<PackageDish> findByMenuPackageVendorId(Long vendorId, Pageable pageable);

    Optional<PackageDish> findByIdAndMenuPackageVendorId(Long id, Long vendorId);

    void deleteByMenuPackageId(Long menuPackageId);
}
