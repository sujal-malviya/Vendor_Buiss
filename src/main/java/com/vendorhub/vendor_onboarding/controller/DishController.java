package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.entity.EventType;
import com.vendorhub.vendor_onboarding.service.DishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private DishService dishService;

    DishController(DishService dishService)
    {
        this.dishService = dishService;
    }

    @PostMapping
    public Dish createDish(@RequestBody Dish dish)
    {
        return dishService.createDish(dish);
    }

    @GetMapping
    public List<Dish> getAllDish()
    {
        return dishService.getAllDish();
    }

    @GetMapping("/{id}")
    public Dish getAllDishById(@PathVariable Long id)
    {
        return dishService.getAllDishById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDish(@PathVariable Long id)
    {
        dishService.deleteDish(id);
    }

    @PutMapping("/{id}")
    public Dish updateDish(@PathVariable Long id,@RequestBody Dish dish)
    {
        return dishService.updateDish(id,dish);
    }

    @PatchMapping("/{id}")
    public Dish updateDishes(@PathVariable Long id,@RequestBody Dish dish)
    {
        return dishService.updateDishes(id,dish);
    }

}
