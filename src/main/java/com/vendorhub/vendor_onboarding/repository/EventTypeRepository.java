package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType,Long> {
}
