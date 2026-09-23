package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.Dish;
import com.vendorhub.vendor_onboarding.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Shared dish catalog: every vendor can read it, only admins can change it (see SecurityConfig)
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    DishController(DishService dishService)
    {
        this.dishService = dishService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dish createDish(@Valid @RequestBody Dish dish)
    {
        return dishService.createDish(dish);
    }

    @GetMapping
    public List<Dish> getAllDishes()
    {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Long id)
    {
        return dishService.getDishById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable Long id)
    {
        dishService.deleteDish(id);
    }

    @PutMapping("/{id}")
    public Dish updateDish(@PathVariable Long id, @Valid @RequestBody Dish dish)
    {
        return dishService.updateDish(id, dish);
    }

    @PatchMapping("/{id}")
    public Dish patchDish(@PathVariable Long id, @RequestBody Dish dish)
    {
        return dishService.patchDish(id, dish);
    }
}
