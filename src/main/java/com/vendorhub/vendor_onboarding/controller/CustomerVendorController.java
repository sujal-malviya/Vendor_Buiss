package com.vendorhub.vendor_onboarding.controller;

import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.dto.VendorSearchResponse;
import com.vendorhub.vendor_onboarding.service.CustomerVendorService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Customers browse vendors: GET /api/customer/vendors?city=Pune&page=0&size=20
@RestController
@RequestMapping("/api/customer/vendors")
public class CustomerVendorController {

    private final CustomerVendorService customerVendorService;

    CustomerVendorController(CustomerVendorService customerVendorService)
    {
        this.customerVendorService = customerVendorService;
    }

    @GetMapping
    public PageResponse<VendorSearchResponse> searchVendors(
            @RequestParam(required = false) String city,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable)
    {
        return customerVendorService.searchVendors(city, pageable);
    }
}
