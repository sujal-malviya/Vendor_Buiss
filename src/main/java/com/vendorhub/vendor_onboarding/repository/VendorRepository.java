package com.vendorhub.vendor_onboarding.repository;

import java.util.Optional;

import com.vendorhub.vendor_onboarding.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	Optional<Vendor> findByEmail(String email);

	Optional<Vendor> findByPhone(String phone);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}