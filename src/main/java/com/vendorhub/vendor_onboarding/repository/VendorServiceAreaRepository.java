package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorServiceAreaRepository extends JpaRepository<VendorServiceArea,Long> {

    List<VendorServiceArea> findByVendorProfileVendorId(Long vendorId);

    Optional<VendorServiceArea> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);

    void deleteByVendorProfileId(Long vendorProfileId);
}
