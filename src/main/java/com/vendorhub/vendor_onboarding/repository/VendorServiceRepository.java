package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorServiceRepository extends JpaRepository<VendorServiceArea,Long> {
}
