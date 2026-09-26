package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.RequiredItemRequest;
import com.vendorhub.vendor_onboarding.dto.RequiredItemResponse;
import com.vendorhub.vendor_onboarding.service.RequiredItemService;
import com.vendorhub.vendor_onboarding.dto.PageResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public RequiredItemResponse createRequiredItem(@Valid @RequestBody RequiredItemRequest request)
    {
        return requiredItemService.createRequiredItem(request);
    }

    @GetMapping
    public PageResponse<RequiredItemResponse> getAllRequiredItems(@ParameterObject @PageableDefault(size = 20, sort = "id") Pageable pageable)
    {
        return requiredItemService.getAllRequiredItems(pageable);
    }

    @GetMapping("/{id}")
    public RequiredItemResponse getRequiredItemById(@PathVariable Long id)
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
    public RequiredItemResponse updateRequiredItem(@PathVariable Long id, @Valid @RequestBody RequiredItemRequest request)
    {
        return requiredItemService.updateRequiredItem(id, request);
    }

    @PatchMapping("/{id}")
    public RequiredItemResponse patchRequiredItem(@PathVariable Long id, @RequestBody RequiredItemRequest request)
    {
        return requiredItemService.patchRequiredItem(id, request);
    }
}
