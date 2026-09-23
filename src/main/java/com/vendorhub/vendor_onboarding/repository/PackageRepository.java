package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package,Long> {
}
