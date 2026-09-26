package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.DishRequest;
import com.vendorhub.vendor_onboarding.dto.DishResponse;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.DishRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    private final DishRepository dishRepository;

    DishService(DishRepository dishRepository)
    {
        this.dishRepository = dishRepository;
    }

    public DishResponse createDish(DishRequest request)
    {
        Dish dish = new Dish();
        request.applyTo(dish);
        return DishResponse.from(dishRepository.save(dish));
    }

    public PageResponse<DishResponse> getAllDishes(Pageable pageable)
    {
        return PageResponse.from(dishRepository.findAll(pageable), DishResponse::from);
    }

    public DishResponse getDishById(Long id)
    {
        return DishResponse.from(findDish(id));
    }

    public void deleteDish(Long id)
    {
        dishRepository.delete(findDish(id));
    }

    public DishResponse updateDish(Long id, DishRequest request)
    {
        Dish existing = findDish(id);
        request.applyTo(existing);
        return DishResponse.from(dishRepository.save(existing));
    }

    public DishResponse patchDish(Long id, DishRequest request)
    {
        Dish existing = findDish(id);
        request.patch(existing);
        return DishResponse.from(dishRepository.save(existing));
    }

    // Public because VendorDishService and PackageDishService need the entity to link to it
    public Dish findDish(Long id)
    {
        return dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish", id));
    }
}
