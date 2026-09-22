package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.VendorPaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorPaymentRepository extends JpaRepository<VendorPaymentPlan,Long> {

}
