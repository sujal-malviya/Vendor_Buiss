package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorDish;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VendorDishRepository extends JpaRepository<VendorDish,Long> {

    Page<VendorDish> findByVendorId(Long vendorId, Pageable pageable);

    Optional<VendorDish> findByIdAndVendorId(Long id, Long vendorId);
}
