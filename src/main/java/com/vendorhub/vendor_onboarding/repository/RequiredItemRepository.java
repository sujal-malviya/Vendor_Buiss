package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequiredItemRepository extends JpaRepository<RequiredItem,Long> {
}
