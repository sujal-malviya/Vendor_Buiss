package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.DishRequest;
import com.vendorhub.vendor_onboarding.dto.DishResponse;
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
    public DishResponse createDish(@Valid @RequestBody DishRequest request)
    {
        return dishService.createDish(request);
    }

    @GetMapping
    public List<DishResponse> getAllDishes()
    {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public DishResponse getDishById(@PathVariable Long id)
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
    public DishResponse updateDish(@PathVariable Long id, @Valid @RequestBody DishRequest request)
    {
        return dishService.updateDish(id, request);
    }

    @PatchMapping("/{id}")
    public DishResponse patchDish(@PathVariable Long id, @RequestBody DishRequest request)
    {
        return dishService.patchDish(id, request);
    }
}
