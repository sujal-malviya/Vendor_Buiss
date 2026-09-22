package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorBankDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorBankRepository extends JpaRepository<VendorBankDetail,Long> {
}
