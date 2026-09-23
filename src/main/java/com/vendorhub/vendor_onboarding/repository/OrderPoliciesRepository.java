package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.OrderPolicies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderPoliciesRepository extends JpaRepository<OrderPolicies,Long> {

    List<OrderPolicies> findByVendorProfileVendorId(Long vendorId);

    Optional<OrderPolicies> findByIdAndVendorProfileVendorId(Long id, Long vendorId);

    boolean existsByVendorProfileId(Long vendorProfileId);

    void deleteByVendorProfileId(Long vendorProfileId);
}
