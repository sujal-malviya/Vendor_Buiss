package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.PackageDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageDishRepository extends JpaRepository<PackageDish,Long> {

    List<PackageDish> findByMenuPackageVendorId(Long vendorId);

    Optional<PackageDish> findByIdAndMenuPackageVendorId(Long id, Long vendorId);

    void deleteByMenuPackageId(Long menuPackageId);
}
