package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorPaymentRepository extends JpaRepository<VendorPaymentPlan,Long> {

    List<VendorPaymentPlan> findByVendorProfileVendorId(Long vendorId);

    Optional<VendorPaymentPlan> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);

    void deleteByVendorProfileId(Long vendorProfileId);
}
