package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorBusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorBusinessInfoRepository extends JpaRepository<VendorBusinessInfo,Long> {

    List<VendorBusinessInfo> findByVendorProfileVendorId(Long vendorId);

    Optional<VendorBusinessInfo> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);
}
