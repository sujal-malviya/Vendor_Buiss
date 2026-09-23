package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorProfileRepository extends JpaRepository<VendorProfile,Long> {

    Optional<VendorProfile> findByVendorId(Long vendorId);

    Optional<VendorProfile> findByIdAndVendorId(Long id, Long vendorId);

    boolean existsByVendorId(Long vendorId);
}
