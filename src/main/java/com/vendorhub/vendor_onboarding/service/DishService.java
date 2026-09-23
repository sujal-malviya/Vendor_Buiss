package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.exception.ResourceNotFoundException;
import com.vendorhub.vendor_onboarding.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;

    DishService(DishRepository dishRepository)
    {
        this.dishRepository = dishRepository;
    }

    public Dish createDish(Dish dish)
    {
        dish.setId(null);   // never let the body pick the id, or a POST could overwrite an existing row
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes()
    {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id)
    {
        return dishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dish", id));
    }

    public void deleteDish(Long id)
    {
        dishRepository.delete(getDishById(id));
    }

    public Dish updateDish(Long id, Dish dish)
    {
        getDishById(id);
        dish.setId(id);
        return dishRepository.save(dish);
    }

    // PATCH: only change the fields that were sent
    public Dish patchDish(Long id, Dish dish)
    {
        Dish existing = getDishById(id);
        if (dish.getName() != null) existing.setName(dish.getName());
        if (dish.getDescription() != null) existing.setDescription(dish.getDescription());
        if (dish.getCategory() != null) existing.setCategory(dish.getCategory());
        if (dish.getCuisine() != null) existing.setCuisine(dish.getCuisine());
        if (dish.getVegetarian() != null) existing.setVegetarian(dish.getVegetarian());
        if (dish.getActive() != null) existing.setActive(dish.getActive());
        return dishRepository.save(existing);
    }
}
