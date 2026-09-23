package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorDishRepository extends JpaRepository<VendorDish,Long> {

    List<VendorDish> findByVendorId(Long vendorId);

    Optional<VendorDish> findByIdAndVendorId(Long id, Long vendorId);
}
