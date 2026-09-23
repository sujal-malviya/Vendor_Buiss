package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import com.vendorhub.vendor_onboarding.service.RequiredItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Shared list: every vendor can read it, only admins can change it (see SecurityConfig)
@RestController
@RequestMapping("/api/vendor/required-items")
public class RequiredItemController {

    private final RequiredItemService requiredItemService;

    RequiredItemController(RequiredItemService requiredItemService)
    {
        this.requiredItemService = requiredItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequiredItem createRequiredItem(@Valid @RequestBody RequiredItem requiredItem)
    {
        return requiredItemService.createRequiredItem(requiredItem);
    }

    @GetMapping
    public List<RequiredItem> getAllRequiredItems()
    {
        return requiredItemService.getAllRequiredItems();
    }

    @GetMapping("/{id}")
    public RequiredItem getRequiredItemById(@PathVariable Long id)
    {
        return requiredItemService.getRequiredItemById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequiredItem(@PathVariable Long id)
    {
        requiredItemService.deleteRequiredItem(id);
    }

    @PutMapping("/{id}")
    public RequiredItem updateRequiredItem(@PathVariable Long id, @Valid @RequestBody RequiredItem requiredItem)
    {
        return requiredItemService.updateRequiredItem(id, requiredItem);
    }

    @PatchMapping("/{id}")
    public RequiredItem patchRequiredItem(@PathVariable Long id, @RequestBody RequiredItem requiredItem)
    {
        return requiredItemService.patchRequiredItem(id, requiredItem);
    }
}
