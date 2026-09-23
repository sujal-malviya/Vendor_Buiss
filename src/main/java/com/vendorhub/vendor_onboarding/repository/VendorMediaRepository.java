package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorMediaRepository extends JpaRepository<VendorMedia,Long> {

    List<VendorMedia> findByVendorProfileVendorId(Long vendorId);

    Optional<VendorMedia> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);

    void deleteByVendorProfileId(Long vendorProfileId);
}
