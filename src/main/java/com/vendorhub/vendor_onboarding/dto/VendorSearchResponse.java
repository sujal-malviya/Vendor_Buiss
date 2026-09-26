package com.vendorhub.vendor_onboarding.dto;

/**
 * One vendor in the customer search results.
 * The JPQL query in VendorProfileRepository builds these directly with "SELECT new ...VendorSearchResponse(...)",
 * so the constructor arguments must stay in the same order as the query.
 */
public record VendorSearchResponse(
        Long vendorProfileId,
        String businessName,
        String city,
        Long startingPrice,
        Long maxPeople) {
}
