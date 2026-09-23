package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorBankRepository extends JpaRepository<VendorBankDetail,Long> {

    List<VendorBankDetail> findByVendorProfileVendorId(Long vendorId);

    Optional<VendorBankDetail> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);

    void deleteByVendorProfileId(Long vendorProfileId);
}
