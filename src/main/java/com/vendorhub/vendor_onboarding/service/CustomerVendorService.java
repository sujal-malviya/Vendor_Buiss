package com.vendorhub.vendor_onboarding.service;

import com.vendorhub.vendor_onboarding.dto.PageResponse;
import com.vendorhub.vendor_onboarding.dto.VendorSearchResponse;
import com.vendorhub.vendor_onboarding.repository.VendorProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerVendorService {

    private final VendorProfileRepository vendorProfileRepository;

    CustomerVendorService(VendorProfileRepository vendorProfileRepository)
    {
        this.vendorProfileRepository = vendorProfileRepository;
    }

    public PageResponse<VendorSearchResponse> searchVendors(String city, Pageable pageable)
    {
        // Keep the client's page and size, but always sort by profile id: the client's sort field names
        // (e.g. "businessName") are response fields, not entity fields, and would break the query.
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        Page<VendorSearchResponse> results = (city == null || city.isBlank())
                ? vendorProfileRepository.searchAllVendors(page)
                : vendorProfileRepository.searchVendorsByCity(city.trim(), page);
        return PageResponse.from(results, result -> result);
    }
}
