package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish,Long> {
}
