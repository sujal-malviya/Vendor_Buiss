package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.entity.RequiredItem;
import com.vendorhub.vendor_onboarding.service.RequireditemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/required-items")
public class RequiredItemController {

    private RequireditemService requireditemService;
    RequiredItemController(RequireditemService requireditemService)
    {
        this.requireditemService = requireditemService;
    }

    @PostMapping
    public RequiredItem createRequirement(@RequestBody RequiredItem requiredItem)
    {
        return requireditemService.createRequirement(requiredItem);
    }

    @GetMapping
    public List<RequiredItem> getAllRequirements()
    {
        return requireditemService.getAllRequirements();
    }

    @GetMapping("/{id}")
    public RequiredItem getAllRequirementsById(@PathVariable Long id)
    {
        return  requireditemService.getAllRequirementsById(id);
    }

    @DeleteMapping("/{id}/")
    public void deleteRequirement(@PathVariable Long id)
    {
        requireditemService.deleteRequirement(id);
    }

    @PutMapping("/{id}")
    public RequiredItem updateRequirement(@PathVariable Long id , @RequestBody RequiredItem requiredItem)
    {
        return requireditemService.updateRequirement(id, requiredItem);
    }

    @PatchMapping("/{id}")
    public RequiredItem updateRequirements(@PathVariable Long id , @RequestBody RequiredItem requiredItem)
    {
        return requireditemService.updateRequirements(id, requiredItem);
    }
}
